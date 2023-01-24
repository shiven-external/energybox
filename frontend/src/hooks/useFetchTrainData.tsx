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

    console.log('fetch');
    fetchData();

    const interval = setInterval((): void => {
      fetchData();
    }, 1000 * 60);

    return (): void => clearInterval(interval);
  }, []);

  return { trains };
};

/* 
      {
        TrainId: '059',
        TrainNumber: '000',
        CarCount: 6,
        DirectionNum: 1,
        CircuitId: 2494,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 777,
        ServiceType: 'Normal'
      },
      {
        TrainId: '060',
        TrainNumber: '000',
        CarCount: 6,
        DirectionNum: 1,
        CircuitId: 2581,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 148,
        ServiceType: 'Special'
      },
      {
        TrainId: '084',
        TrainNumber: '000',
        CarCount: 6,
        DirectionNum: 1,
        CircuitId: 2580,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 3319,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '149',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 3107,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 6157,
        ServiceType: 'Special'
      },
      {
        TrainId: '197',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 3127,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 6157,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '290',
        TrainNumber: 'PM38',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 167,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 547,
        ServiceType: 'NoPassengers'
      },
      {
        TrainId: '294',
        TrainNumber: 'PM54',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 1092,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 1910,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '314',
        TrainNumber: 'PM45',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 1011,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 6157,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '315',
        TrainNumber: 'LP51',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 3010,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 1070,
        ServiceType: 'NoPassengers'
      },
      {
        TrainId: '316',
        TrainNumber: 'PM47',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 361,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 1211,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '317',
        TrainNumber: 'PM48',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 1871,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 1629,
        ServiceType: 'NoPassengers'
      },
      {
        TrainId: '318',
        TrainNumber: 'PM29',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 3516,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 4,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '327',
        TrainNumber: 'PB15',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 3016,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 216,
        ServiceType: 'NoPassengers'
      },
      {
        TrainId: '328',
        TrainNumber: 'PM26',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 652,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 1944,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '331',
        TrainNumber: 'SV01',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 1298,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 236,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '332',
        TrainNumber: 'PM39',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 1824,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 393,
        ServiceType: 'NoPassengers'
      },
      {
        TrainId: '334',
        TrainNumber: 'GV01',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 2627,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 11,
        ServiceType: 'NoPassengers'
      },
      {
        TrainId: '335',
        TrainNumber: 'SV02',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 2469,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 89,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '336',
        TrainNumber: 'PM37',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 1711,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 1179,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '337',
        TrainNumber: 'PM42',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 2707,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 21,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '338',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 1758,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 1727,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '339',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 1712,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 1179,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '340',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 1543,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 1088,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '341',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 1703,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 464,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '345',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 1132,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 471,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '347',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 1166,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 236,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '348',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 2218,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 6,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '349',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 1,
        CircuitId: 2220,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 6,
        ServiceType: 'Unknown'
      },
      {
        TrainId: '350',
        TrainNumber: '000',
        CarCount: 0,
        DirectionNum: 2,
        CircuitId: 2353,
        DestinationStationCode: null,
        LineCode: null,
        SecondsAtLocation: 6,
        ServiceType: 'Unknown'
      }
    ]
  };
*/
