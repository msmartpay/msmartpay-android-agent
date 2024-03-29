package in.msmartpay.agent.myWallet;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.widget.Toast;

import java.util.Objects;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;


public class BalanceRequestActivity extends BaseActivity implements BalanceRequestFragment.Communication, BalanceHistoryFragment.Communicator2 {

    ViewPager viewPager = null;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_request_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Balance Request");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        viewPager = (ViewPager) findViewById(R.id.pagerr);
        viewPager.setAdapter(new Myadapter(getSupportFragmentManager(),BalanceRequestActivity.this));    // Set up the ViewPager with the sections adapter.
        tabLayout = (TabLayout) findViewById(R.id.tabss);
        tabLayout.setupWithViewPager(viewPager);

    }

    class Myadapter extends FragmentStatePagerAdapter {
        private Context ctx;

        public Myadapter(FragmentManager fm, Context ctx) {
            super(fm);
            this.ctx = ctx;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (isConnectionAvailable()) {
                if (position == 0) {
                    fragment = new BalanceRequestFragment();
                }
                if (position == 1) {
                    fragment = new BalanceHistoryFragment();
                }
            } else {
                Toast.makeText(ctx, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Balance Request";
                case 1:
                    return "Request History";
            }
            return null;
        }
    }
        @Override
        public boolean onSupportNavigateUp() {
            onBackPressed();
            return true;
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

