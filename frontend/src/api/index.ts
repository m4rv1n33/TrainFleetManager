import api from './client';
import {
  AssignmentCreatePayload,
  AssignmentStatus,
  Train,
  TrainAssignment,
  TrainLine,
  TrainStatus,
} from '../types';

export const getTrains = async (): Promise<Train[]> => {
  const { data } = await api.get<Train[]>('/trains');
  return data;
};

export const createTrain = async (train: Omit<Train, 'id' | 'currentAssignment'>): Promise<Train> => {
  const { data } = await api.post<Train>('/trains', train);
  return data;
};

export const updateTrainStatus = async (
  vehicleNumber: string,
  status: TrainStatus,
): Promise<Train> => {
  const { data } = await api.put<Train>(`/trains/number/${vehicleNumber}/status`, { status });
  return data;
};

export const deleteTrain = async (vehicleNumber: string): Promise<void> => {
  await api.delete(`/trains/number/${vehicleNumber}`);
};

export const getLines = async (): Promise<TrainLine[]> => {
  const { data } = await api.get<TrainLine[]>('/lines');
  return data;
};

export const createLine = async (line: Omit<TrainLine, 'id'>): Promise<TrainLine> => {
  const { data } = await api.post<TrainLine>('/lines', line);
  return data;
};

export const getAssignments = async (): Promise<TrainAssignment[]> => {
  const { data } = await api.get<TrainAssignment[]>('/assignments');
  return data;
};

export const createAssignment = async (assignment: AssignmentCreatePayload): Promise<TrainAssignment> => {
  const { data } = await api.post<TrainAssignment>('/assignments', assignment);
  return data;
};

export const updateAssignmentStatus = async (
  id: number,
  status: AssignmentStatus,
): Promise<TrainAssignment> => {
  const { data } = await api.put<TrainAssignment>(`/assignments/${id}/status`, JSON.stringify(status), {
    headers: { 'Content-Type': 'application/json' },
  });
  return data;
};

export const deleteAssignment = async (id: number): Promise<void> => {
  await api.delete(`/assignments/${id}`);
};
