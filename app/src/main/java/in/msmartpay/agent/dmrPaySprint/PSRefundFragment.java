package in.msmartpay.agent.dmrPaySprint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import in.msmartpay.agent.R;

public class PSRefundFragment extends Fragment {

    private EditText editRefundTransaction;
    private Button btnRefundTransaction;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ps_dmr_refund_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = getActivity();

        editRefundTransaction = (EditText) view.findViewById(R.id.edit_transaction_refund);
        btnRefundTransaction = (Button) view.findViewById(R.id.btn_refund_otp);

        btnRefundTransaction.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editRefundTransaction.getText().toString().trim())) {
                Toast.makeText(context, "Please Enter Service Delivery TID", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, PSRefundLiveStatusActivity.class);
                intent.putExtra("TranNo", editRefundTransaction.getText().toString().trim());
                intent.putExtra("fromIntent", "RefundFragment");
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        editRefundTransaction.setText("");
    }
}