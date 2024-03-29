package in.msmartpay.agent.dmr2Moneytrasfer;

import android.os.Bundle;
import android.widget.TextView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;

/**
 * Created by Smartkinda on 6/19/2017.
 */

public class TransactionReceiptActivity extends BaseActivity {

    private TextView tviewDeliveryTID, tviewTransferedType, tviewTransferedAmount, tviewBeneficiaryName, tviewBankName, tviewAccountNo, tviewIFSCCode, tviewTransactionStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmr2_transaction_reciept);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction Receipt");

//        dataAgentID = getIntent().getStringExtra("AgentID");

        tviewDeliveryTID = (TextView) findViewById(R.id.tv_delivery_tid);
        tviewTransferedType = (TextView) findViewById(R.id.tv_transfer_type);
        tviewTransferedAmount = (TextView) findViewById(R.id.tv_transfer_amount);
        tviewBeneficiaryName = (TextView) findViewById(R.id.tv_bene_name);
        tviewBankName = (TextView) findViewById(R.id.tv_bank_name);
        tviewAccountNo = (TextView) findViewById(R.id.tv_account_no);
        tviewIFSCCode = (TextView) findViewById(R.id.tv_ifsc_code);
        tviewTransactionStatus = (TextView) findViewById(R.id.tv_transaction_status);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
