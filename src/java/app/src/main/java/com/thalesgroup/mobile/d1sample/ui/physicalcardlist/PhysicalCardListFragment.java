package com.thalesgroup.mobile.d1sample.ui.physicalcardlist;

import android.view.View;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.ui.basecardlist.AbstractCardListFragment;
import com.thalesgroup.mobile.d1sample.ui.login.LoginFragment;
import com.thalesgroup.mobile.d1sample.ui.physicalcarddetail.PhysicalCardDetailFragment;
import com.thalesgroup.mobile.d1sample.ui.virtualcarddetail.VirtualCardDetailFragment;
import com.thalesgroup.mobile.d1sample.ui.virtualcardlist.VirtualCardListViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment for the list of virtual cards.
 */
public class PhysicalCardListFragment extends AbstractCardListFragment<PhysicalCardListViewModel> {
    /**
     * Creates a new instance of {@code VirtualCardFragment}.
     *
     * @return Instance of {@code VirtualCardFragment}.
     */
    public static PhysicalCardListFragment newInstance() {
        return new PhysicalCardListFragment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void showFragment(@NonNull final String cardId) {
        showFragment(PhysicalCardDetailFragment.newInstance(cardId), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initOtherViews(@NonNull final View view, @NonNull final RecyclerView recyclerView) {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected PhysicalCardListViewModel createViewModel() {
        return new ViewModelProvider(this).get(PhysicalCardListViewModel.class);
    }
}