package cobong.jeongwoojin.cobongmemo.cobongmemo.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.BaseRecyclerViewAdapter;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.MemoItemBinding;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;

public class MemoAdapter extends BaseRecyclerViewAdapter<MemoListItem, MemoAdapter.MemoViewHolder> {

    private MemoViewModel viewModel;
    private ItemClickListener listener;

    public MemoAdapter(List<MemoListItem> list, MemoViewModel viewModel) {
        super(list);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MemoItemBinding binding = MemoItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);



        //선언하고
        listener = new ItemClickListener() {
            @Override
            public void onMemoClick(MemoListItem item) {
                viewModel.sendMemo(item);
            }
        };

        //아이템 레이아웃에 뷰모델,리스너 등록
        binding.setListener(listener);

        binding.setViewmodel(viewModel);
        return new MemoAdapter.MemoViewHolder(binding);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public void onBindView(MemoAdapter.MemoViewHolder holder, int position) {
        holder.binding.setMemo(getItem(position));
    }

    class MemoViewHolder extends RecyclerView.ViewHolder {

        MemoItemBinding binding;

        public MemoViewHolder(MemoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
