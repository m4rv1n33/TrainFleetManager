import { FormEvent, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { createTrain, deleteTrain, getTrains, updateTrainStatus } from '../api';
import StatusBadge from '../components/StatusBadge';
import { Train, TrainStatus } from '../types';
import type { AxiosError } from 'axios';

const trainStatuses: TrainStatus[] = ['IN_SERVICE', 'OUT_OF_SERVICE', 'MAINTENANCE'];

function TrainsPage() {
  const queryClient = useQueryClient();
  const { data: trains, isLoading } = useQuery({ queryKey: ['trains'], queryFn: getTrains });
  const [statusError, setStatusError] = useState('');

  const [form, setForm] = useState<Omit<Train, 'id' | 'currentAssignment'>>({
    vehicleNumber:  '',
    maxSpeed: 160,
    length: 150,
    status: 'IN_SERVICE',
  });

  const getErrorMessage = (err: unknown) => {
    const ax = err as AxiosError<any> | undefined;
    if (!ax) return '';
    const data = ax.response?.data;
    if (data?.message) return data.message as string;
    if (typeof data === 'string') return data;
    return ax.message;
  };

  const createMutation = useMutation({
    mutationFn: createTrain,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trains'] });
      setForm((prev) => ({ ...prev, vehicleNumber: '' }));
    },
  });

  const statusMutation = useMutation({
    mutationFn: ({ vehicleNumber, status }: { vehicleNumber: string; status: TrainStatus }) =>
      updateTrainStatus(vehicleNumber, status),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['trains'] }),
  });

  const deleteMutation = useMutation({
    mutationFn: (vehicleNumber: string) => deleteTrain(vehicleNumber),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['trains'] }),
  });

  const createError = getErrorMessage(createMutation.error);
  const deleteError = getErrorMessage(deleteMutation.error);

  const busy = createMutation.isPending || statusMutation.isPending || deleteMutation.isPending;

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (!form.vehicleNumber.trim()) return;
    createMutation.mutate({ ...form, maxSpeed: Number(form.maxSpeed), length: Number(form.length) });
  };

  const handleStatusChange = (train: Train, next: TrainStatus) => {
    const hasActiveAssignment = train.currentAssignment?.status === 'ACTIVE';
    if (hasActiveAssignment && next === 'MAINTENANCE') {
      setStatusError('Cannot set a train to MAINTENANCE while it has an ACTIVE assignment.');
      return;
    }
    setStatusError('');
    statusMutation.mutate({ vehicleNumber: train.vehicleNumber, status: next });
  };

  return (
    <div className="rail-stack">
      <div className="page-heading">
        <div>
          <h1 className="page-title">Trains</h1>
        </div>
      </div>

      <div className="grid two">
        <form className="card rail-outline" onSubmit={handleSubmit}>
          <div className="section-title">
            <h2>Add train</h2>
          </div>
          <div className="form-grid">
            <label>
              Vehicle number
              <input
                required
                value={form.vehicleNumber}
                onChange={(e) => setForm({ ...form, vehicleNumber: e.target.value })}
                placeholder="511-001"
              />
            </label>
            <label>
              Max speed (km/h)
              <input
                type="number"
                min={40}
                value={form.maxSpeed}
                onChange={(e) => setForm({ ...form, maxSpeed: Number(e.target.value) })}
              />
            </label>
            <label>
              Length (m)
              <input
                type="number"
                min={10}
                value={form.length}
                onChange={(e) => setForm({ ...form, length: Number(e.target.value) })}
              />
            </label>
            <label>
              Status
              <select
                className="select"
                value={form.status}
                onChange={(e) => setForm({ ...form, status: e.target.value as TrainStatus })}
              >
                {trainStatuses.map((s) => (
                  <option key={s} value={s}>
                    {s.replace(/_/g, ' ')}
                  </option>
                ))}
              </select>
            </label>
          </div>
          <div className="inline-actions" style={{ justifyContent: 'space-between', marginTop: 12 }}>
            <span className="helper-text">
              {createMutation.isError && (createError || 'Could not create train. Check vehicleNumber uniqueness.')}
            </span>
            <button className="primary" type="submit" disabled={busy}>
              {createMutation.isPending ? 'Saving...' : 'Add train'}
            </button>
          </div>
        </form>

        <div className="card rail-outline">
          <div className="section-title">
            <h2>Fleet metrics</h2>
          </div>
          <div className="grid three">
            <div className="stat-card">
              <div className="stat-label">Total trains</div>
              <div className="stat-value">{isLoading ? '...' : trains?.length ?? 0}</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">In service</div>
              <div className="stat-value">
                {isLoading ? '...' : trains?.filter((t) => t.status === 'IN_SERVICE').length ?? 0}
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Out of service</div>
              <div className="stat-value">
                {isLoading ? '...' : trains?.filter((t) => t.status === 'OUT_OF_SERVICE').length ?? 0}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="rail-board">
        <div className="rail-board__header">
          <h2 className="rail-board__title">Fleet</h2>
        </div>
        <div className="rail-board__body">
          {statusError && (
            <div className="helper-text" style={{ color: '#b00015', marginBottom: 8 }}>
              {statusError}
            </div>
          )}
          {deleteMutation.isError && (
            <div className="helper-text" style={{ color: '#b00015', marginBottom: 8 }}>
              {deleteError || 'Could not delete train.'}
            </div>
          )}
          {isLoading ? (
            <div className="helper-text">Loading trains...</div>
          ) : trains && trains.length > 0 ? (
            <table className="table">
              <thead>
                <tr>
                  <th>Vehicle</th>
                  <th>Max speed</th>
                  <th>Length</th>
                  <th>Status</th>
                  <th>Assignment</th>
                  <th>Update status</th>
                </tr>
              </thead>
              <tbody>
                {trains.map((train) => (
                  <tr key={train.id ?? train.vehicleNumber}>
                    <td>{train.vehicleNumber}</td>
                    <td>{train.maxSpeed ?? '-'} km/h</td>
                    <td>{train.length ?? '-'} m</td>
                    <td>
                      <StatusBadge value={train.status} />
                    </td>
                    <td>{train.currentAssignment?.line?.name ?? '-'}</td>
                    <td>
                      <select
                        className="select"
                        value={train.status}
                        onChange={(e) =>
                          handleStatusChange(train, e.target.value as TrainStatus)
                        }
                        disabled={statusMutation.isPending}
                      >
                        {trainStatuses.map((s) => {
                          const blockMaintenance = train.currentAssignment?.status === 'ACTIVE' && s === 'MAINTENANCE';
                          return (
                            <option key={s} value={s} disabled={blockMaintenance}>
                              {s.replace(/_/g, ' ')}
                            </option>
                          );
                        })}
                      </select>
                    </td>
                    <td>
                      <button
                        className="ghost"
                        onClick={() => deleteMutation.mutate(train.vehicleNumber)}
                        disabled={busy}
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <div className="helper-text">No trains yet. Add your first train above.</div>
          )}
        </div>
      </div>
    </div>
  );
}

export default TrainsPage;
