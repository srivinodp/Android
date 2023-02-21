public class CalculatorActivity extends AppCompatActivity {

    private CalculatorFragment mCalculatorFragment;
    private Button mCalculateButton;
    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mResultTextView = findViewById(R.id.result_text_view);

        mCalculateButton = findViewById(R.id.calculate_button);
        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double result = mCalculatorFragment.getResult();
                Intent intent = new Intent();
                intent.putExtra("result", result);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        mCalculateButton.setEnabled(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mCalculatorFragment = new CalculatorFragment();
        fragmentTransaction.add(R.id.fragment_container, mCalculatorFragment);
        fragmentTransaction.commit();
    }

    public void setCalculateButtonEnabled(boolean enabled) {
        mCalculateButton.setEnabled(enabled);
    }

    public void setResultText(String text) {
        mResultTextView.setText(text);
    }

}
