interface DropdownProps extends React.ComponentProps<'select'> {
  onChange: (event: React.ChangeEvent<HTMLSelectElement>) => void;
  addDefaultSelectedOption?: boolean;
}

export const Dropdown = ({
  onChange,
  children,
  addDefaultSelectedOption,
  ...props
}: DropdownProps): JSX.Element => {
  return (
    <select onChange={onChange} {...props}>
      {addDefaultSelectedOption && (
        <option selected value="">
          None
        </option>
      )}
      {children}
    </select>
  );
};

interface OptionProps extends React.ComponentProps<'option'> {
  value: string;
}

export const Option = ({
  value,
  children,
  ...props
}: OptionProps): JSX.Element => {
  return (
    <option value={value} {...props}>
      {children}
    </option>
  );
};
