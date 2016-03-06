package com.vzl.activate;

import com.vzl.activate.model.Contract;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mRecyclerView = (RecyclerView) findViewById(R.id.recylerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerAdapter(getLayoutInflater()));

    }

    public static final class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ContractViewHolder> {

        private final LayoutInflater mLayoutInflater;
        List<Contract> mContractList = new LinkedList<>();

        public RecyclerAdapter(LayoutInflater layoutInflater) {
            mLayoutInflater = layoutInflater;
        }

        public void setContractList(List<Contract> contractList) {
            mContractList = contractList;
        }

        @Override
        public ContractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.contract_history_item, parent);
            return new ContractViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContractViewHolder holder, int position) {
            holder.populate(mContractList.get(position));
        }

        @Override
        public int getItemCount() {
            return mContractList.size();
        }

        public class ContractViewHolder extends RecyclerView.ViewHolder {

            private final TextView mAmount;
            private final TextView mDate;
            private final TextView mDiscount;

            public ContractViewHolder(View itemView) {
                super(itemView);
                mAmount = (TextView) itemView.findViewById(R.id.amount);
                mDate = (TextView) itemView.findViewById(R.id.date);
                mDiscount = (TextView) itemView.findViewById(R.id.discount);
            }

            public void populate(Contract contract) {
                mAmount.setText(contract.getAmount());
                mDate.setText("Timestamp: " + contract.getDate());
                mDiscount.setText(contract.getDiscount());
            }
        }
    }

}
