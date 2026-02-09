export type TrainStatus = 'IN_SERVICE' | 'OUT_OF_SERVICE' | 'MAINTENANCE';
export type AssignmentStatus = 'PLANNED' | 'ACTIVE' | 'COMPLETED' | 'CANCELLED';

export interface TrainLine {
  id?: number;
  name: string;
  startStation: string;
  endStation: string;
}

export interface Train {
  id?: number;
  vehicleNumber: string;
  maxSpeed?: number;
  length?: number;
  status: TrainStatus;
  currentAssignment?: TrainAssignment | null;
}

export interface TrainAssignment {
  id?: number;
  date?: string;
  status: AssignmentStatus;
  train?: Train;
  line?: TrainLine;
  trainVehicleNumber?: string;
  lineName?: string;
}

export interface AssignmentCreatePayload {
  date?: string;
  status: AssignmentStatus;
  train: { vehicleNumber: string };
  line: { id: number };
}

export interface TrainStatusDto {
  vehicleNumber: string;
  status: string;
  line: string | null;
}
