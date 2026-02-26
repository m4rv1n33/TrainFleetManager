import { screen } from '@testing-library/react';
import { vi } from 'vitest';
import DashboardPage from '../Dashboard';
import { renderWithProviders } from '../../test/test-utils';
import * as api from '../../api';

vi.mock('../../api', () => ({
  getTrains: vi.fn(),
  getLines: vi.fn(),
  getAssignments: vi.fn(),
}));

describe('DashboardPage', () => {
  it('renders summary counts from API data', async () => {
    const mockedGetTrains = vi.mocked(api.getTrains);
    const mockedGetLines = vi.mocked(api.getLines);
    const mockedGetAssignments = vi.mocked(api.getAssignments);

    mockedGetTrains.mockResolvedValue([
      { id: 1, vehicleNumber: '511-001', status: 'IN_SERVICE' },
      { id: 2, vehicleNumber: '511-002', status: 'OUT_OF_SERVICE' },
    ]);
    mockedGetLines.mockResolvedValue([
      { id: 10, name: 'IC5', startStation: 'Basel SBB', endStation: 'Zuerich HB' },
    ]);
    mockedGetAssignments.mockResolvedValue([
      { id: 100, status: 'ACTIVE', trainVehicleNumber: '511-001', lineName: 'IC5', date: '2026-02-20' },
      { id: 101, status: 'PLANNED', trainVehicleNumber: '511-002', lineName: 'IC5', date: '2026-02-21' },
    ]);

    renderWithProviders(<DashboardPage />);

    expect(await screen.findByText('Operations dashboard')).toBeInTheDocument();
    expect(await screen.findByText('In service: 1')).toBeInTheDocument();
    expect(await screen.findByText('Active: 1')).toBeInTheDocument();
    expect(await screen.findAllByText('IC5')).toHaveLength(2);
  });
});
