package in.msmartpay.agent.rechargeBillPay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.msmartpay.agent.R;

/**
 * Created by Roran on 12/6/2017.
 */

public class CustomOperatorClass extends BaseAdapter {

    private Context mContext;
    private List<OperatorListModel> operatorList;

    public CustomOperatorClass(Context context, List<OperatorListModel> operator) {
        mContext = context;
        operatorList = operator;
    }

    @Override
    public int getCount() {
        return operatorList.size();
    }

    @Override
    public Object getItem(int i) {
        return operatorList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spinner_textview_layout, null);
        }
        else {
            view = convertView;
        }
        TextView custom_spinner = view.findViewById(R.id.custom_spinner);
        custom_spinner.setText(operatorList.get(position).getOperatorName());

        return view;
    }
}
