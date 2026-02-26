import { render, screen } from '@testing-library/react';
import StatusBadge from '../StatusBadge';

describe('StatusBadge', () => {
  it('renders known status with mapped class', () => {
    render(<StatusBadge value="IN_SERVICE" />);

    const badge = screen.getByText('IN SERVICE');
    expect(badge).toHaveClass('badge', 'success');
  });

  it('falls back to neutral for unknown status', () => {
    render(<StatusBadge value="UNKNOWN" />);

    const badge = screen.getByText('UNKNOWN');
    expect(badge).toHaveClass('badge', 'neutral');
  });
});
