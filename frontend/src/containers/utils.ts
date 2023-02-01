import { ServiceType } from '../types/enums/ServiceType.enum';
import { TrainLineColor } from '../types/enums/TrainLineColor.enum';
import { Train } from '../types/interfaces/Trains.interface';

export const selectTrainsByLineColor = (
  trains: Train[],
  selectedTrainLineColorValue?: TrainLineColor
): Train[] => {
  if (!selectedTrainLineColorValue) return trains;

  return trains.filter((train): boolean => {
    if (
      !train.LineCode &&
      selectedTrainLineColorValue === TrainLineColor.Unknown
    )
      return true;

    if (train.LineCode === selectedTrainLineColorValue) return true;

    return false;
  });
};

export const selectTrainsByServiceType = (
  trains: Train[],
  selectedServiceTypeValue?: ServiceType
): Train[] => {
  if (!selectedServiceTypeValue) return trains;

  return trains.filter((train): boolean => {
    if (train.ServiceType === selectedServiceTypeValue) return true;

    return false;
  });
};

export const selectTrainsByCarCount = (
  trains: Train[],
  inputtedMaxCarCountValue?: number,
  inputtedMinCarCountValue?: number
): Train[] => {
  const maxValue = inputtedMaxCarCountValue ?? Infinity;
  const minValue = inputtedMinCarCountValue ?? 0;

  return trains.filter((train): boolean => {
    if (train.CarCount >= minValue && train.CarCount <= maxValue) return true;

    return false;
  });
};
