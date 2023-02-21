package CalculatorApp;

public class CalculatorFragment extends Fragment {

    private TextView mDisplayTextView;
    private Button mAddButton;
    private Button mSubtractButton;
    private Button mMultiplyButton;
    private Button mDivideButton;
    private Button mClearButton;

    private double mOperand1;
    private double mOperand2;
    private char mOperator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        mDisplayTextView = view.findViewById(R.id.display_text_view);

        mAddButton = view.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performOperation('+');
            }
        });

        mSubtractButton = view.findViewById(R.id.subtract_button);
        mSubtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performOperation('-');
            }
        });
    
        mMultiplyButton = view.findViewById(R.id.multiply_button);
        mMultiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performOperation('*');
            }
        });
    
        mDivideButton = view.findViewById(R.id.divide_button);
        mDivideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performOperation('/');
            }
        });
    
        mClearButton = view.findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });
    
        return view;
    }
    
    private void performOperation(char operator) {
        mOperand1 = Double.parseDouble(mDisplayTextView.getText().toString());
        mOperator = operator;
        mDisplayTextView.setText("");
        ((CalculatorActivity)getActivity()).setCalculateButtonEnabled(true);
    }
    
    private void clear() {
        mOperand1 = 0;
        mOperand2 = 0;
        mOperator = ' ';
        mDisplayTextView.setText("");
        ((CalculatorActivity)getActivity()).setCalculateButtonEnabled(false);
    }
    
    public double getResult() {
        mOperand2 = Double.parseDouble(mDisplayTextView.getText().toString());
        switch (mOperator) {
            case '+':
                return mOperand1 + mOperand2;
            case '-':
                return mOperand1 - mOperand2;
            case '*':
                return mOperand1 * mOperand2;
            case '/':
                return mOperand1 / mOperand2;
            default:
                return 0;
        }
    }
}
    
