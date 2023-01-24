import { useState } from 'react';
import styles from './ViewTrains.module.scss';
import { ServiceType } from '../types/enums/ServiceType.enum';
import { TrainLineColor } from '../types/enums/TrainLineColor.enum';
import { useFetchTrainData } from '../hooks/useFetchTrainData';
import { useFilteredTrains } from '../hooks/useFilteredTrains';
import { TrainsTable } from './TrainsTable';
import { FilterBar } from './FilterBar';

export const ViewTrains: React.FC = (): JSX.Element => {
  const { trains } = useFetchTrainData();

  const [serviceTypeFilter, setServiceTypeFilter] = useState<
    ServiceType | undefined
  >();

  const [trainLineColorFilter, setTrainLineColorFilter] = useState<
    TrainLineColor | undefined
  >();

  const [carCountFilterMax, setCarCountFilterMax] = useState<
    number | undefined
  >();

  const [carCountFilterMin, setCarCountFilterMin] = useState<
    number | undefined
  >();

  const { filteredTrains } = useFilteredTrains({
    trains,
    serviceTypeFilter,
    carCountFilterMax,
    carCountFilterMin,
    trainLineColorFilter
  });

  return (
    <div className={styles.tableContainer}>
      <FilterBar
        onChangeServiceTypeFilter={(serviceType: ServiceType): void => {
          setServiceTypeFilter(serviceType);
        }}
        onChangeTrainLineColorFilter={(
          trainLineColor: TrainLineColor
        ): void => {
          setTrainLineColorFilter(trainLineColor);
        }}
        onChangeCarCountFilter={(max: number, min: number): void => {
          setCarCountFilterMax(max);
          setCarCountFilterMin(min);
        }}
      />

      <TrainsTable trains={filteredTrains} />
    </div>
  );
};
