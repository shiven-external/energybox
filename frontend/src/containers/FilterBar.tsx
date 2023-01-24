import { useEffect, useState } from 'react';
import { Dropdown, Option } from '../components/Dropdown';
import { ServiceType } from '../types/enums/ServiceType.enum';
import { TrainLineColor } from '../types/enums/TrainLineColor.enum';
import styles from './FilterBar.module.scss';

interface FilterBarProps {
  onChangeServiceTypeFilter: (serviceType: ServiceType) => void;
  onChangeTrainLineColorFilter: (trainLineColor: TrainLineColor) => void;
  onChangeCarCountFilter: (max: number, min: number) => void;
}

export const FilterBar = ({
  onChangeServiceTypeFilter,
  onChangeTrainLineColorFilter,
  onChangeCarCountFilter
}: FilterBarProps): JSX.Element => {
  return (
    <div>
      <ServiceTypeDropdown onChange={onChangeServiceTypeFilter} />
      <TrainLineColorDropdown onChange={onChangeTrainLineColorFilter} />
      <CarCountInputs onChange={onChangeCarCountFilter} />
    </div>
  );
};

const ServiceTypeDropdown = ({
  onChange
}: {
  onChange: (serviceType: ServiceType) => void;
}): JSX.Element => {
  return (
    <div className={styles.serviceTypeFilterContainer}>
      <span>Service Type: </span>
      <Dropdown
        onChange={(event: React.ChangeEvent<HTMLSelectElement>): void => {
          onChange(event.target.value as ServiceType);
        }}
        addDefaultSelectedOption
      >
        {Object.values(ServiceType).map((option): JSX.Element => {
          return (
            <Option key={option} value={option}>
              {option}
            </Option>
          );
        })}
      </Dropdown>
    </div>
  );
};

const TrainLineColorDropdown = ({
  onChange
}: {
  onChange: (trainLineColor: TrainLineColor) => void;
}): JSX.Element => {
  return (
    <div className={styles.trainLineColorFilterContainer}>
      <span>Train Line Color: </span>

      <Dropdown
        onChange={(event: React.ChangeEvent<HTMLSelectElement>): void => {
          onChange(event.target.value as TrainLineColor);
        }}
        addDefaultSelectedOption
      >
        {Object.values(TrainLineColor).map((option): JSX.Element => {
          return (
            <Option key={option} value={option}>
              {option}
            </Option>
          );
        })}
      </Dropdown>
    </div>
  );
};

const CarCountInputs = ({
  onChange
}: {
  onChange: (max: number, min: number) => void;
}): JSX.Element => {
  const [max, setMax] = useState<number>(Infinity);
  const [min, setMin] = useState<number>(0);
  const [error, setError] = useState<string | undefined>();

  useEffect((): void => {
    if (max < min) {
      setError('Max cannot be less than min');

      return;
    }

    setError(undefined);
    onChange(max, min);
  }, [min, max, onChange]);

  return (
    <div className={styles.carCountFilterContainer}>
      <div>
        <span>Car Count Min: </span>
        <input
          type="number"
          id="min"
          name="quantity"
          min="0"
          onChange={(event: React.ChangeEvent<HTMLInputElement>): void => {
            const value =
              event.target.value === '' ? 0 : parseInt(event.target.value);
            setMin(value);
          }}
        />
      </div>

      <div>
        <span className={styles.carCountMaxLabel}>Car Count Max: </span>
        <input
          type="number"
          id="max"
          name="quantity"
          min="0"
          onChange={(event: React.ChangeEvent<HTMLInputElement>): void => {
            const value =
              event.target.value === ''
                ? Infinity
                : parseInt(event.target.value);

            setMax(value);
          }}
        />
      </div>
      {error && <p className={styles.error}>{error}</p>}
    </div>
  );
};
