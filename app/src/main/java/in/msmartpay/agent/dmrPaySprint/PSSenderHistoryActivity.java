package in.msmartpay.agent.dmrPaySprint;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;

import in.msmartpay.agent.R;
import in.msmartpay.agent.dmrPaySprint.adapter.PSSenderHistoryAdapter;
import in.msmartpay.agent.network.AppMethods;
import in.msmartpay.agent.network.NetworkConnection;
import in.msmartpay.agent.network.RetrofitClient;
import in.msmartpay.agent.network.model.dmr.SenderFindRequest;
import in.msmartpay.agent.network.model.dmr.SenderHistoryResponse;
import in.msmartpay.agent.network.model.dmr.Statement;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.ProgressDialogFragment;
import in.msmartpay.agent.utility.Util;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class PSSenderHistoryActivity extends BaseActivity {

    private Context context;
    private String MobileNo, agentID, txnKey;
    private RecyclerView listViewSender;
    private ProgressDialogFragment pd;
    private ArrayList<Statement> arrayList = new ArrayList<Statement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_dmr_sender_history_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Sender History");

        context = PSSenderHistoryActivity.this;
        agentID = Util.LoadPrefData(getApplicationContext(), Keys.AGENT_ID);
        txnKey = Util.LoadPrefData(getApplicationContext(), Keys.TXN_KEY);
        MobileNo = getIntent().getStringExtra("MobileNo");
        Log.d("MobileNo--", MobileNo);
        listViewSender = findViewById(R.id.list_view);

        getSenderHistory();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        arrayList.clear();
        getSenderHistory();

    }


    //===========SenderHistory========================
    //For Json Request
    private void getSenderHistory() {
        if (NetworkConnection.isConnectionAvailable(getApplicationContext())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...", "Getting Latest Details of Sender...");
            ProgressDialogFragment.showDialog(pd, getSupportFragmentManager());
            SenderFindRequest request = new SenderFindRequest();
            request.setAgentID(Util.LoadPrefData(context, Keys.AGENT_ID));
            request.setKey(Util.LoadPrefData(context, Keys.TXN_KEY));
            request.setSenderId(MobileNo);
            RetrofitClient.getClient(context).senderHistory(Util.LoadPrefData(context,Keys.DYNAMIC_DMR_VENDOR)+ AppMethods.SenderHistory,request).enqueue(new Callback<SenderHistoryResponse>() {
                @Override
                public void onResponse(Call<SenderHistoryResponse> call, retrofit2.Response<SenderHistoryResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        SenderHistoryResponse res = response.body();
                        if (res.getStatus().equals("0")) {
                            arrayList = (ArrayList<Statement>) res.getStatement();
                            listViewSender.setAdapter(new PSSenderHistoryAdapter(context, arrayList));
                        }
                    } else {
                        L.toastS(context, "No Response");
                    }
                }

                @Override
                public void onFailure(Call<SenderHistoryResponse> call, Throwable t) {
                    pd.dismiss();
                    L.toastS(context, "Error : " + t.getMessage());
                }
            });
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}