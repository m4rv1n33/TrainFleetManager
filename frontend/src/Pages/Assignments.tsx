import { FormEvent, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  createAssignment,
  deleteAssignment,
  getAssignments,
  getLines,
  getTrains,
  updateAssignmentStatus,
} from '../api';
import StatusBadge from '../components/StatusBadge';
import { AssignmentStatus } from '../types';
import type { AxiosError } from 'axios';

const assignmentStatuses: AssignmentStatus[] = ['PLANNED', 'ACTIVE', 'COMPLETED', 'CANCELLED'];

const formatDate = (value?: string) => {
  if (!value) return '-';
  const parsed = new Date(value);
  if (Number.isNaN(parsed.getTime())) return value;
  return new Intl.DateTimeFormat('en-GB').format(parsed);
};

function AssignmentsPage() {
  const queryClient = useQueryClient();
  const { data: assignments, isLoading } = useQuery({ queryKey: ['assignments'], queryFn: getAssignments });
  const { data: trains } = useQuery({ queryKey: ['trains'], queryFn: getTrains });
  const { data: lines } = useQuery({ queryKey: ['lines'], queryFn: getLines });

  const [form, setForm] = useState({
    trainVehicleNumber: '',
    lineId: '',
    date: '',
    status: 'PLANNED' as AssignmentStatus,
  });

  const createMutation = useMutation({
    mutationFn: () =>
      createAssignment({
        status: form.status,
        date: form.date || undefined,
        train: { vehicleNumber: form.trainVehicleNumber },
        line: { id: Number(form.lineId) },
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['assignments'] });
      setForm({ trainVehicleNumber: '', lineId: '', date: '', status: 'PLANNED' });
    },
  });

  const statusMutation = useMutation({
    mutationFn: ({ id, status }: { id: number; status: AssignmentStatus }) =>
      updateAssignmentStatus(id, status),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['assignments'] }),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => deleteAssignment(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['assignments'] }),
  });

  const deleteError = (deleteMutation.error as any)?.response?.data?.message as string | undefined;

  const busy = createMutation.isPending || statusMutation.isPending || deleteMutation.isPending;

  const createErrorMessage = (() => {
    const err = createMutation.error as AxiosError<any> | undefined;
    if (!err) return '';
    return err.response?.data?.message || 'Validation failed. Ensure train, line, and status are set.';
  })();

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (!form.trainVehicleNumber || !form.lineId) return;
    createMutation.mutate();
  };

  return (
    <div className="page-stack">
      <div className="page-heading">
        <div>
          <h1 className="page-title">Assignments</h1>
        </div>
      </div>

      <div className="grid two assignments-grid">
        <div className="panel form-panel">
          <div className="section-title">
            <h2>Create assignment</h2>
          </div>

          {createMutation.isError && <div className="callout error">{createErrorMessage}</div>}

          <form className="form-grid" onSubmit={handleSubmit}>
            <label>
              Train
              <select
                className="select"
                required
                value={form.trainVehicleNumber}
                onChange={(e) => setForm({ ...form, trainVehicleNumber: e.target.value })}
              >
                <option value="">Choose a train</option>
                {trains?.map((t) => (
                  <option key={t.vehicleNumber} value={t.vehicleNumber}>
                    {t.vehicleNumber} ({t.status.replace(/_/g, ' ')})
                  </option>
                ))}
              </select>
            </label>
            <label>
              Line
              <select
                className="select"
                required
                value={form.lineId}
                onChange={(e) => setForm({ ...form, lineId: e.target.value })}
              >
                <option value="">Choose a line</option>
                {lines?.map((l) => (
                  <option key={l.id ?? l.name} value={l.id}>
                    {l.name} ({l.startStation} to {l.endStation})
                  </option>
                ))}
              </select>
            </label>
            <label>
              Start date
              <input type="date" value={form.date} onChange={(e) => setForm({ ...form, date: e.target.value })} />
            </label>
            <label>
              Status
              <select
                className="select"
                value={form.status}
                onChange={(e) => setForm({ ...form, status: e.target.value as AssignmentStatus })}
              >
                {assignmentStatuses.map((s) => (
                  <option key={s} value={s}>
                    {s.replace(/_/g, ' ')}
                  </option>
                ))}
              </select>
            </label>
            <div className="inline-actions" style={{ gridColumn: '1 / -1', justifyContent: 'flex-end' }}>
              <button className="primary" type="submit" disabled={busy}>
                {createMutation.isPending ? 'Saving...' : 'Create assignment'}
              </button>
            </div>
          </form>
        </div>

        <div className="panel table-panel">
          <div className="section-title">
            <h2>Assignment board</h2>
          </div>
          <div className="table-shell">
            {isLoading ? (
              <div className="helper-text">Loading assignments...</div>
            ) : assignments && assignments.length > 0 ? (
              <table className="table modern compact">
                <thead>
                  <tr>
                    <th>Train</th>
                    <th>Line</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Set status</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {assignments.map((a) => (
                    <tr key={a.id ?? `${a.train?.vehicleNumber}-${a.date}`}>
                      <td>{a.trainVehicleNumber ?? a.train?.vehicleNumber ?? '-'}</td>
                      <td>{a.lineName ?? a.line?.name ?? '-'}</td>
                      <td>{formatDate(a.date)}</td>
                      <td>
                        <StatusBadge value={a.status} />
                      </td>
                      <td>
                        <select
                          className="select compact"
                          value={a.status}
                          onChange={(e) =>
                            statusMutation.mutate({ id: Number(a.id), status: e.target.value as AssignmentStatus })
                          }
                          disabled={statusMutation.isPending}
                        >
                          {assignmentStatuses.map((s) => (
                            <option key={s} value={s}>
                              {s.replace(/_/g, ' ')}
                            </option>
                          ))}
                        </select>
                      </td>
                      <td>
                        <button className="ghost" onClick={() => a.id && deleteMutation.mutate(a.id)} disabled={busy}>
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <div className="empty-state">
                <p>No assignments yet.</p>
              </div>
            )}
            {deleteMutation.isError && (
              <div className="helper-text" style={{ color: 'var(--error)', marginTop: 8 }}>
                {deleteError || 'Could not delete assignment.'}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default AssignmentsPage;
