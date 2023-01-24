export interface Train {
  CarCount: number;
  CircuitId: number;
  DestinationStationCode: string | null;
  DirectionNum: number;
  LineCode: string | null;
  SecondsAtLocation: number;
  ServiceType: string;
  TrainId: string;
  TrainNumber: string;
}
