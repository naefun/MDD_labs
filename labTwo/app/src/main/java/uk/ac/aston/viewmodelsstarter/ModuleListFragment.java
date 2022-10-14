package uk.ac.aston.viewmodelsstarter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import uk.ac.aston.viewmodelsstarter.databinding.FragmentModulelistBinding;
import uk.ac.aston.viewmodelsstarter.model.ModuleViewModel;

public class ModuleListFragment extends Fragment {

	private FragmentModulelistBinding binding;
	private ModuleViewModel model;

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState
	) {

		binding = FragmentModulelistBinding.inflate(inflater, container, false);
		return binding.getRoot();

	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		model = new ViewModelProvider(requireActivity()).get(ModuleViewModel.class);
		binding.textviewFirst.setText(model.getCurrentModule().getValue().getTitle());

		binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				NavHostFragment.findNavController(ModuleListFragment.this)
						.navigate(R.id.action_FirstFragment_to_SecondFragment);
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

}