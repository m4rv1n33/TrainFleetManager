import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import Layout from '../Layout';

const renderLayout = () =>
  render(
    <MemoryRouter>
      <Layout>
        <div>Content</div>
      </Layout>
    </MemoryRouter>,
  );

describe('Layout', () => {
  beforeEach(() => {
    window.localStorage.clear();
    document.documentElement.removeAttribute('data-theme');
  });

  it('toggles theme and persists preference', async () => {
    window.localStorage.setItem('tfm-theme', 'light');
    const user = userEvent.setup();

    renderLayout();

    const toggle = screen.getByRole('button', { name: 'Dark mode' });
    await user.click(toggle);

    expect(document.documentElement.getAttribute('data-theme')).toBe('dark');
    expect(window.localStorage.getItem('tfm-theme')).toBe('dark');
    expect(screen.getByRole('button', { name: 'Light mode' })).toBeInTheDocument();
  });
});
