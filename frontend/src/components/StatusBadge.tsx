import { AssignmentStatus, TrainStatus } from '../types';

type Status = TrainStatus | AssignmentStatus | string;

const palette: Record<string, string> = {
  IN_SERVICE: 'badge success',
  OUT_OF_SERVICE: 'badge error',
  MAINTENANCE: 'badge warning',
  PLANNED: 'badge neutral',
  ACTIVE: 'badge success',
  COMPLETED: 'badge muted',
  CANCELLED: 'badge error',
};

interface Props {
  value: Status;
}

function StatusBadge({ value }: Props) {
  const key = String(value).toUpperCase();
  const cls = palette[key] || 'badge neutral';
  return <span className={cls}>{key.replace(/_/g, ' ')}</span>;
}

export default StatusBadge;
