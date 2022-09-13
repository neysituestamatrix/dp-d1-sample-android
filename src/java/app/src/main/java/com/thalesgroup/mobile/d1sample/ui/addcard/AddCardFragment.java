package com.thalesgroup.mobile.d1sample.ui.addcard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.gemalto.d1.validation.databinding.FragmentAddCardBinding;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

/**
 * Add card Fragment.
 */
public class AddCardFragment extends AbstractBaseFragment<AddCardViewModel> {

    private static final String ARG_CARD_ID = "ARG_CARD_ID";

    private String mCardId;

    /**
     * Creates a new instance of {@code AddCardFragment}.
     *
     * @param cardId Card ID.
     * @return Instance of {@code AddCardFragment}.
     */
    public static AddCardFragment newInstance(@NonNull final String cardId) {
        final AddCardFragment fragment = new AddCardFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_CARD_ID, cardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCardId = getArguments().getString(ARG_CARD_ID);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected AddCardViewModel createViewModel() {
        return new ViewModelProvider(this).get(AddCardViewModel.class);
    }


    @Override
    public View onCreateView(@NotNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final FragmentAddCardBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_add_card, container, false);
        binding.setLifecycleOwner(this);
        binding.setMViewModel(mViewModel);

        final View view = binding.getRoot();

        view.findViewById(R.id.bt_add_card_to_wallet).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.digitizeCard(mCardId);
        });

        mViewModel.getIsOperationSuccesfull().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "Card digitized.", Toast.LENGTH_LONG).show();
                popFromBackstack();
            }
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        });

        // Inflate the layout for this fragment
        return view;
    }
}