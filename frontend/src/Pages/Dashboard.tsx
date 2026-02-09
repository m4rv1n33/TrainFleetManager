import { useQuery } from '@tanstack/react-query';
import { getAssignments, getLines, getTrains } from '../api';
import StatCard from '../components/StatCard';
import StatusBadge from '../components/StatusBadge';
import { AssignmentStatus, TrainStatus } from '../types';

const formatDate = (value?: string) => {
  if (!value) return '-';
  const parsed = new Date(value);
  if (Number.isNaN(parsed.getTime())) return value;
  return new Intl.DateTimeFormat('en-GB').format(parsed);
};

function DashboardPage() {
  const { data: trains, isLoading: loadingTrains } = useQuery({ queryKey: ['trains'], queryFn: getTrains });
  const { data: lines, isLoading: loadingLines } = useQuery({ queryKey: ['lines'], queryFn: getLines });
  const { data: assignments, isLoading: loadingAssignments } = useQuery({ queryKey: ['assignments'], queryFn: getAssignments });

  const totalTrains = trains?.length ?? 0;
  const totalLines = lines?.length ?? 0;
  const totalAssignments = assignments?.length ?? 0;

  const trainsByStatus: Record<TrainStatus, number> = {
    IN_SERVICE: 0,
    OUT_OF_SERVICE: 0,
    MAINTENANCE: 0,
  };
  trains?.forEach((t) => {
    const key = t.status as TrainStatus;
    trainsByStatus[key] = (trainsByStatus[key] ?? 0) + 1;
  });

  const activeAssignments = assignments?.filter((a) => a.status === 'ACTIVE').length ?? 0;

  const latestAssignments = assignments?.slice().reverse().slice(0, 6) ?? [];

  const loading = loadingAssignments || loadingLines || loadingTrains;

  return (
    <div className="rail-stack">
      <div className="page-heading">
        <div>
          <h1 className="page-title">Operations dashboard</h1>
        </div>
      </div>

      <div className="grid three">
        <StatCard label="Trains" value={loading ? '...' : totalTrains} helper={`In service: ${trainsByStatus.IN_SERVICE}`} />
        <StatCard label="Lines" value={loading ? '...' : totalLines} helper={`Total`} />
        <StatCard label="Assignments" value={loading ? '...' : totalAssignments} helper={`Active: ${activeAssignments}`} />
      </div>

      <div className="grid two">
        <div className="rail-board">
          <div className="rail-board__header">
            <h2 className="rail-board__title">Recent assignments</h2>
          </div>
          <div className="rail-board__body">
            {loading ? (
              <div className="helper-text">Loading...</div>
            ) : latestAssignments.length === 0 ? (
              <div className="helper-text">No assignments yet. Create one to start tracking.</div>
            ) : (
              <table className="table">
                <thead>
                  <tr>
                    <th>Train</th>
                    <th>Line</th>
                    <th>Date</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {latestAssignments.map((a) => (
                    <tr key={a.id ?? `${a.train?.vehicleNumber}-${a.date}`}>
                      <td>{a.trainVehicleNumber ?? a.train?.vehicleNumber ?? '-'}</td>
                      <td>{a.lineName ?? a.line?.name ?? '-'}</td>
                      <td>{formatDate(a.date)}</td>
                      <td>
                        <StatusBadge value={a.status} />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>

        <div className="card rail-outline">
          <div className="section-title">
            <h2>Fleet status</h2>
          </div>
          {loading ? (
            <div className="helper-text">Loading...</div>
          ) : (
            <div className="grid">
              {(
                [
                  ['IN_SERVICE', 'Ready for duty', trainsByStatus.IN_SERVICE],
                  ['MAINTENANCE', 'Under maintenance', trainsByStatus.MAINTENANCE],
                  ['OUT_OF_SERVICE', 'Blocked / issues', trainsByStatus.OUT_OF_SERVICE],
                ] as [TrainStatus, string, number][]
              ).map(([status, helper, value]) => (
                <div className="card" key={status} style={{ background: 'var(--panel-soft)' }}>
                  <div className="inline-actions" style={{ justifyContent: 'space-between' }}>
                    <StatusBadge value={status} />
                    <div className="stat-value" style={{ margin: 0 }}>{value}</div>
                  </div>
                  <div className="helper-text">{helper}</div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default DashboardPage;
