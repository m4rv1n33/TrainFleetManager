import { FormEvent, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { createLine, getLines } from '../api';
import { TrainLine } from '../types';

function LinesPage() {
  const queryClient = useQueryClient();
  const { data: lines, isLoading } = useQuery({ queryKey: ['lines'], queryFn: getLines });
  const [form, setForm] = useState<Omit<TrainLine, 'id'>>({ name: '', startStation: '', endStation: '' });

  const createMutation = useMutation({
    mutationFn: createLine,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['lines'] });
      setForm({ name: '', startStation: '', endStation: '' });
    },
  });

  const createError = (createMutation.error as any)?.response?.data?.message as string | undefined;

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (!form.name.trim()) return;
    createMutation.mutate(form);
  };

  return (
    <div className="rail-stack">
      <div className="page-heading">
        <div>
          <h1 className="page-title">Lines</h1>
        </div>
      </div>

      <div className="grid two">
        <form className="card rail-outline" onSubmit={handleSubmit}>
          <div className="section-title">
            <h2>Add line</h2>
          </div>
          <div className="form-grid">
            <label>
              Name
              <input
                required
                value={form.name}
                onChange={(e) => setForm({ ...form, name: e.target.value })}
                placeholder="IC5"
              />
            </label>
            <label>
              Start station
              <input
                required
                value={form.startStation}
                onChange={(e) => setForm({ ...form, startStation: e.target.value })}
                placeholder="Basel SBB"
              />
            </label>
            <label>
              End station
              <input
                required
                value={form.endStation}
                onChange={(e) => setForm({ ...form, endStation: e.target.value })}
                placeholder="Zürich HB"
              />
            </label>
          </div>
          <div className="inline-actions" style={{ justifyContent: 'flex-end', marginTop: 12 }}>
            <div className="helper-text" style={{ marginRight: 'auto' }}>
              {createMutation.isError && (createError || 'Could not create line.')}
            </div>
            <button className="primary" type="submit" disabled={createMutation.isPending}>
              {createMutation.isPending ? 'Saving...' : 'Add line'}
            </button>
          </div>
        </form>

        <div className="rail-board">
          <div className="rail-board__header">
            <h2 className="rail-board__title">Line roster</h2>
          </div>
          <div className="rail-board__body">
            {isLoading ? (
              <div className="helper-text">Loading lines...</div>
            ) : lines && lines.length > 0 ? (
              <table className="table">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Start</th>
                    <th>End</th>
                  </tr>
                </thead>
                <tbody>
                  {lines.map((line) => (
                    <tr key={line.id ?? line.name}>
                      <td>{line.name}</td>
                      <td>{line.startStation}</td>
                      <td>{line.endStation}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <div className="helper-text">No lines yet. Create one to start assigning trains.</div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default LinesPage;
