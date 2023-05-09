package in.msmartpayagent.rechargeBillPay;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Date;

import in.msmartpayagent.MainActivity;
import in.msmartpayagent.R;
import in.msmartpayagent.databinding.ActivityLicBinding;
import in.msmartpayagent.databinding.ActivityPaytmBinding;
import in.msmartpayagent.myWallet.TransactionHistoryReceipt;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.MainRequest2;
import in.msmartpayagent.network.model.MainResponse2;
import in.msmartpayagent.network.model.PaytmWalletRequest;
import in.msmartpayagent.network.model.lic.LicBillFetchRequest;
import in.msmartpayagent.network.model.lic.LicBillFetchRequestData;
import in.msmartpayagent.network.model.lic.LicBillFetchResponse;
import in.msmartpayagent.network.model.lic.LicBillFetchResponseData;
import in.msmartpayagent.network.model.lic.LicBillpayRequest;
import in.msmartpayagent.network.model.lic.LicBillpayResponse;
import in.msmartpayagent.network.model.lic.LicBillpayResponseData;
import in.msmartpayagent.network.model.wallet.TransactionItems;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LICPremiumActivity extends BaseActivity {

    private ActivityLicBinding binding;
    private EditText et_lic_email,et_lic_policy_number,et_lic_amount,et_lic_due_date,et_lic_holder_name;
    private Button btn_lic_fetch,btn_lic_payment;
    private TextView tv_lic_message;
    private ProgressDialogFragment pd;
    private String billId;
    private String validationId;
    private String billFetch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("LIC Premium Payment");

        et_lic_policy_number = binding.etLicPolicyNumber;
        et_lic_email = binding.etLicEmailId;
        et_lic_holder_name = binding.etLicPolicyHolderName;
        et_lic_due_date = binding.etLicDueDate;
        et_lic_amount = binding.etLicDueAmount;
        btn_lic_fetch = binding.btnLicFetchBill;
        btn_lic_payment = binding.btnLicPayment;

        tv_lic_message = binding.tvLicMessage;
        Util.hideView(btn_lic_payment);
        Util.hideView(et_lic_holder_name);
        Util.hideView(et_lic_due_date);
        Util.hideView(et_lic_amount);
        Util.hideView(tv_lic_message);

        et_lic_email.setText(Util.LoadPrefData(this, Keys.AGENT_EMAIL));

        btn_lic_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_lic_policy_number.getText().toString().trim())) {
                    L.toastS(getApplicationContext(), "Please lic policy number");
                } else if (TextUtils.isEmpty(et_lic_email.getText().toString().trim())) {
                    L.toastS(getApplicationContext(), "Please enter email id");
                } else {
                    fetchLicBill();
                }
            }
        });

        btn_lic_payment.setOnClickListener(view -> licPaymentRequest());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }

    private void fetchLicBill() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "PayTM Wallet Payment...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
        LicBillFetchRequest request2 = new LicBillFetchRequest();
        request2.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request2.setKey(Util.LoadPrefData(this, Keys.TXN_KEY));

        LicBillFetchRequestData data = new LicBillFetchRequestData();
        data.setCanumber(et_lic_policy_number.getText().toString());
        data.setAd1(et_lic_email.getText().toString());

        request2.setData(data);
        RetrofitClient.getClient(this)
                .fetchLicBill(request2).enqueue(new Callback<LicBillFetchResponse>() {
            @Override
            public void onResponse(Call<LicBillFetchResponse> call, Response<LicBillFetchResponse> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    LicBillFetchResponse res = response.body();
                    if (res.getStatus() != null && res.getStatus().equals("0")) {

                        LicBillFetchResponseData data = res.getData();
                        et_lic_amount.setText(data.getAmount());
                        et_lic_due_date.setText(data.getDuedate());
                        et_lic_holder_name.setText(data.getName());
                        tv_lic_message.setText(res.getMessage());

                        billFetch = Util.getGson().toJson(data.getBillFetch());

                        disableBillDetails();
                    } else if (res.getStatus() != null && res.getStatus().equals("1")) {
                        L.toastS(getApplicationContext(), res.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LicBillFetchResponse> call, Throwable t) {
                pd.dismiss();
                L.toastS(getApplicationContext(), getString(R.string.technical_error) + t.getLocalizedMessage());

            }
        });
    }

    private void licPaymentRequest() {
        pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "PayTM Wallet Payment...");
        ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());

        MainRequest2 request2 = new MainRequest2();
        request2.setAgentID(Util.LoadPrefData(this, Keys.AGENT_ID));
        request2.setKey(Util.LoadPrefData(this, Keys.TXN_KEY));
        request2.setIp(Util.getIpAddress(this));

        LicBillpayRequest data = new LicBillpayRequest();
        data.setCanumber(et_lic_policy_number.getText().toString());
        data.setAd2(et_lic_email.getText().toString());
        data.setAd2("");
        data.setAd3("");
        data.setAmount(et_lic_amount.getText().toString());
        data.setMode("Online");
        data.setLatitude(Util.LoadPrefData(getApplicationContext(), Keys.LATITUDE));
        data.setLongitude(Util.LoadPrefData(getApplicationContext(), Keys.LONGITUDE));

        request2.setData(data);

        RetrofitClient.getClient(this)
                .payLicBill(request2).enqueue(new Callback<LicBillpayResponse>() {
            @Override
            public void onResponse(Call<LicBillpayResponse> call, Response<LicBillpayResponse> response) {
                pd.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    LicBillpayResponse res = response.body();
                    if (res.getStatus() != null && res.getStatus().equals("0")) {

                        LicBillpayResponseData data = res.getData();

                        TransactionItems transactionModel = new TransactionItems();
                        transactionModel.setTran_id(res.getReferenceid());
                        transactionModel.setMobile_operator("LIC Premium");
                        transactionModel.setMobile_number(et_lic_policy_number.getText().toString());
                        transactionModel.setService("LIC");
                        transactionModel.setAction_on_bal_amt("Debit");
                        transactionModel.setTran_status(res.getTxn_status());
                        transactionModel.setTxnAmount(et_lic_amount.getText().toString());
                        transactionModel.setNet_amout(et_lic_amount.getText().toString());
                        transactionModel.setDeductedAmt(et_lic_amount.getText().toString());
                        transactionModel.setDot(new Date().toString());
                        transactionModel.setTot(new Date().toString());
                        transactionModel.setAgent_balAmt_b_Ded("0");
                        transactionModel.setAgent_F_balAmt("0");
                        transactionModel.setBankRefId(data.getOperatorid());
                        transactionModel.setOperatorId(data.getOperatorid());
                        transactionModel.setCommission("0");
                        transactionModel.setServiceCharge("0");
                        transactionModel.setRemark(res.getMessage());
                        transactionModel.setBene_Account("NA");
                        transactionModel.setBene_Bank_IFSC("NA");
                        transactionModel.setBene_Name("NA");
                        transactionModel.setBene_Bank_Name("NA");

                        Intent intent = new Intent( getApplicationContext(), TransactionHistoryReceipt.class);
                        intent.putExtra("position", 1);
                        intent.putExtra("historyModel", Util.getGson().toJson(transactionModel));
                        startActivity(intent);
                        finish();

                    }else{
                        paymentResponseDialog(res.getMessage(), res.getStatus());
                    }

                }else {
                    paymentResponseDialog(getString(R.string.technical_error),"1");
                }
            }

            @Override
            public void onFailure(Call<LicBillpayResponse> call, Throwable t) {
                pd.dismiss();
                paymentResponseDialog(getString(R.string.technical_error),"1");
            }
        });
    }

    private void disableBillDetails() {
        Util.showView(tv_lic_message);
        Util.hideView(btn_lic_fetch);
        et_lic_policy_number.setEnabled(false);
        et_lic_email.setEnabled(false);
        //et_lic_amount.setEnabled(false);
        et_lic_due_date.setEnabled(false);
        et_lic_holder_name.setEnabled(false);
        Util.showView(btn_lic_payment);
        Util.showView(et_lic_amount);
        Util.showView(et_lic_holder_name);
        Util.showView(et_lic_due_date);
    }
    private void paymentResponseDialog(String msg, String status) {
        Dialog dialog_status = new Dialog(this);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.sk_maindialog);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);


        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        text.setText((String) msg);
        if ("0".equals(status))
            statusImage.setImageResource(R.drawable.trnsuccess);
        else
            statusImage.setImageResource(R.drawable.failed);
        final Button trans_status = (Button) dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(v12 -> {
            dialog_status.dismiss();
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        dialog_status.show();
    }

}