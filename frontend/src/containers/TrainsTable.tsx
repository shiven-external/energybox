import { Train } from '../types/interfaces/Trains.interface';
import styles from './ViewTrains.module.scss';

export const TrainsTable = ({ trains }: { trains: Train[] }): JSX.Element => {
  return (
    <table>
      <thead className={styles.tableHeader}>
        <tr className={styles.tableHeaderRow}>
          <th>id</th>
          <th>color</th>
          <th>service type</th>
          <th>car count</th>
          <th>Circuit Id</th>
        </tr>
      </thead>
      <tbody className={styles.tableItems}>
        {trains.map((item: Train): JSX.Element => {
          return (
            <tr key={item.TrainId}>
              <td>{item.TrainId}</td>
              <td>{item.LineCode}</td>
              <td>{item.ServiceType}</td>
              <td>{item.CarCount}</td>
              <td>{item.CircuitId}</td>
            </tr>
          );
        })}
      </tbody>
    </table>
  );
};
