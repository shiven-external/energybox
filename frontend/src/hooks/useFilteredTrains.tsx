import { useEffect, useState } from 'react';
import {
  selectTrainsByCarCount,
  selectTrainsByLineColor,
  selectTrainsByServiceType
} from '../containers/utils';
import { FilterType } from '../types/enums/FilterType.enum';
import { ServiceType } from '../types/enums/ServiceType.enum';
import { TrainLineColor } from '../types/enums/TrainLineColor.enum';
import { Train } from '../types/interfaces/Trains.interface';

interface FetchFilterTrainsReturnData {
  filteredTrains: Train[];
}

interface FetchFilterTrainsProps {
  trains: Train[];
  carCountFilterMax?: number;
  carCountFilterMin?: number;
  serviceTypeFilter?: ServiceType;
  trainLineColorFilter?: TrainLineColor;
}

export const useFilteredTrains = ({
  trains,
  carCountFilterMax,
  carCountFilterMin,
  serviceTypeFilter,
  trainLineColorFilter
}: FetchFilterTrainsProps): FetchFilterTrainsReturnData => {
  const [filteredTrains, setFilteredTrains] = useState<Train[]>(trains);

  useEffect((): void => {
    let selectedTrains = trains;

    selectedTrains = selectTrainsByCarCount(
      selectedTrains,
      carCountFilterMax,
      carCountFilterMin
    );

    selectedTrains = selectTrainsByServiceType(
      selectedTrains,
      serviceTypeFilter
    );

    selectedTrains = selectTrainsByLineColor(
      selectedTrains,
      trainLineColorFilter
    );

    setFilteredTrains(selectedTrains);
  }, [
    carCountFilterMax,
    carCountFilterMin,
    serviceTypeFilter,
    trainLineColorFilter,
    trains
  ]);

  return {
    filteredTrains
  };
};
