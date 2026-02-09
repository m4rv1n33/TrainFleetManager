import { NavLink } from 'react-router-dom';
import { ReactNode, useEffect, useState } from 'react';

interface LayoutProps {
  children: ReactNode;
}

const navItems = [
  { path: '/dashboard', label: 'Dashboard' },
  { path: '/trains', label: 'Trains' },
  { path: '/lines', label: 'Lines' },
  { path: '/assignments', label: 'Assignments' },
];

function Layout({ children }: LayoutProps) {
  const getInitialTheme = () => {
    if (typeof window === 'undefined') return 'light';
    const stored = window.localStorage.getItem('tfm-theme');
    if (stored === 'light' || stored === 'dark') return stored;
    return window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  };

  const [theme, setTheme] = useState<'light' | 'dark'>(getInitialTheme);

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    window.localStorage.setItem('tfm-theme', theme);
  }, [theme]);

  const toggleTheme = () => setTheme((prev) => (prev === 'light' ? 'dark' : 'light'));

  return (
    <div className="app-shell">
      <header className="app-header">
        <div className="brand">
          <div className="brand-logo">TFM</div>
          <div className="brand-copy">
            <h1 className="brand-title">Train Fleet Manager</h1>
          </div>
        </div>

        <nav className="primary-nav">
          {navItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')}
            >
              <span>{item.label}</span>
            </NavLink>
          ))}
        </nav>

        <div className="header-actions">
          <button type="button" className="theme-toggle" onClick={toggleTheme}>
            {theme === 'light' ? 'Dark mode' : 'Light mode'}
          </button>
        </div>
      </header>

      <main className="app-main">
        <div className="page-shell">{children}</div>
      </main>
    </div>
  );
}

export default Layout;
