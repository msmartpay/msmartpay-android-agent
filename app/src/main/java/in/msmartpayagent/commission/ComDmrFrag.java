package in.msmartpayagent.commission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.msmartpayagent.R;
import in.msmartpayagent.commission.adapter.DmrAdapter;
import in.msmartpayagent.network.NetworkConnection;
import in.msmartpayagent.network.RetrofitClient;
import in.msmartpayagent.network.model.commission.CommissionModel;
import in.msmartpayagent.network.model.commission.CommissionRequest;
import in.msmartpayagent.network.model.commission.CommissionResponse;
import in.msmartpayagent.network.model.commission.DmrModel;
import in.msmartpayagent.utility.BaseFragment;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.L;
import in.msmartpayagent.utility.ProgressDialogFragment;
import in.msmartpayagent.utility.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ComDmrFrag extends BaseFragment {
    private RecyclerView rv_comm;
    private TextView tv_14;
    private ProgressDialogFragment pd;
    private String txn_key,agentID;

    public ComDmrFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.commision_frag, container, false);
        rv_comm = view.findViewById(R.id.rv_comm);
        rv_comm.setLayoutManager(new LinearLayoutManager(getActivity()));
        tv_14 = view.findViewById(R.id.tv_14);
        tv_14.setText("Charge");
        agentID = Util.LoadPrefData(getActivity(), Keys.AGENT_ID);
        txn_key = Util.LoadPrefData(getActivity(), Keys.TXN_KEY);
        getCommissions();
        return  view;
    }
    private void getCommissions() {

        if (NetworkConnection.isConnectionAvailable2(getActivity())) {
            pd = ProgressDialogFragment.newInstance("Loading. Please wait...","Fetching Commissions...");
            ProgressDialogFragment.showDialog(pd,getChildFragmentManager());


            CommissionRequest request = new CommissionRequest();
            request.setAgent_id(agentID);
            request.setTxn_key(txn_key);
            request.setService("DMR");

            RetrofitClient.getClient(getActivity())
                    .getCommissions(request).enqueue(new Callback<CommissionResponse>() {
                @Override
                public void onResponse(@NotNull Call<CommissionResponse> call, @NotNull retrofit2.Response<CommissionResponse> response) {
                    pd.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        CommissionResponse res = response.body();
                        if (res.getResponseCode() != null && res.getResponseCode().equals("0")) {
                            ArrayList<DmrModel> list = new ArrayList<>();
                            if (res.getCommList()!=null)
                                for (CommissionModel resObj:res.getCommList()){
                                    DmrModel model = new DmrModel();
                                    model.setService(resObj.getService());
                                    model.setOperstor(resObj.getOperator());
                                    model.setRange(resObj.getStartRange()+" - "+resObj.getEndRange());
                                    model.setCharge(resObj.getComm()+" - "+resObj.getCommType());
                                    list.add(model);
                                }

                            DmrAdapter adapter = new DmrAdapter(getActivity(),list);
                            rv_comm.setAdapter(adapter);
                        } else {
                            L.toastS(getActivity(), res.getResponseMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<CommissionResponse> call, @NotNull Throwable t) {
                    L.toastS(getActivity(), "data failuer " + t.getLocalizedMessage());
                    pd.dismiss();
                }
            });
        }
    }

}