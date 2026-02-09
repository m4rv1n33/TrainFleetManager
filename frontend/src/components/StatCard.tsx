interface StatCardProps {
  label: string;
  value: string | number;
  helper?: string;
}

function StatCard({ label, value, helper }: StatCardProps) {
  return (
    <div className="card stat-card">
      <div className="stat-label">{label}</div>
      <div className="stat-value">{value}</div>
      {helper && <div className="stat-helper">{helper}</div>}
    </div>
  );
}

export default StatCard;
