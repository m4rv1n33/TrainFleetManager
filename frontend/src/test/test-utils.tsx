import React, { PropsWithChildren } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import { render } from '@testing-library/react';

const createTestQueryClient = () =>
  new QueryClient({
    defaultOptions: {
      queries: {
        retry: false,
      },
    },
  });

const Providers = ({ children, route = '/' }: PropsWithChildren<{ route?: string }>) => {
  const queryClient = createTestQueryClient();
  return (
    <QueryClientProvider client={queryClient}>
      <MemoryRouter initialEntries={[route]}>{children}</MemoryRouter>
    </QueryClientProvider>
  );
};

export const renderWithProviders = (ui: React.ReactElement, options?: { route?: string }) =>
  render(ui, {
    wrapper: ({ children }) => <Providers route={options?.route}>{children}</Providers>,
  });
