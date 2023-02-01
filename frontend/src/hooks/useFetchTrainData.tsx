import { useEffect, useState } from 'react';
import { Train } from '../types/interfaces/Trains.interface';

interface FetchTrainReturnData {
  trains: Train[];
}

export const useFetchTrainData = (): FetchTrainReturnData => {
  const [trains, setTrains] = useState<Train[]>([]);

  useEffect((): (() => void) => {
    const fetchData = async (): Promise<void> => {
      if (
        !process.env.REACT_APP_WMATA_API_DOMAIN ||
        !process.env.REACT_APP_WMATA_TRAIN_POSITION_DATA ||
        !process.env.REACT_APP_WMATA_API_KEY
      )
        return;

      const response: Response = await fetch(
        `${process.env.REACT_APP_WMATA_API_DOMAIN}${process.env.REACT_APP_WMATA_TRAIN_POSITION_DATA}`,
        {
          headers: {
            api_key: process.env.REACT_APP_WMATA_API_KEY
          }
        }
      );

      const data = (await response.json()) as { TrainPositions: Train[] };

      setTrains(data.TrainPositions);
    };

    fetchData();

    const interval = setInterval((): void => {
      fetchData();
    }, 1000 * 60);

    return (): void => clearInterval(interval);
  }, []);

  return { trains };
};
