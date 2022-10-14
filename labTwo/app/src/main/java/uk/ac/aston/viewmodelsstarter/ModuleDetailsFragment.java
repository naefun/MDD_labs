package uk.ac.aston.viewmodelsstarter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import uk.ac.aston.viewmodelsstarter.databinding.FragmentModuledetailsBinding;
import uk.ac.aston.viewmodelsstarter.model.Module;
import uk.ac.aston.viewmodelsstarter.model.ModuleViewModel;

public class ModuleDetailsFragment extends Fragment {

	private FragmentModuledetailsBinding binding;
	private ModuleViewModel model;

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState
	) {

		binding = FragmentModuledetailsBinding.inflate(inflater, container, false);
		return binding.getRoot();

	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		model = new ViewModelProvider(requireActivity()).get(ModuleViewModel.class);
		model.getCurrentModule().observe(getViewLifecycleOwner(), module -> {
			// Update the UI.
			String title = module.getCode() + ": " + module.getTitle() + " (" + module.getCredits() + " credits)";
			binding.textviewTitle.setText(title);
		});
		updateUI();

		binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				NavHostFragment.findNavController(ModuleDetailsFragment.this)
						.navigate(R.id.action_SecondFragment_to_FirstFragment);
			}
		});

		binding.editTitle.addTextChangedListener(new MyTextWatcher());
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	private void updateUI() {
		Module m = model.getCurrentModule().getValue();
		String title = m.getCode() + ": " + m.getTitle() + " (" + m.getCredits() + " credits)";
		binding.textviewTitle.setText(title);
		binding.textviewDescription.setText(m.getDescription());
		binding.textviewLo.setText(m.getLearningOutcomes());
		binding.editTitle.setText(m.getTitle());
	}

	class MyTextWatcher implements TextWatcher{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// Fires right as the text is being changed (even supplies the range of text)
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
			// Fires right before text is changing
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Fires right after the text has changed
			model.setCurrentModuleTitle(s.toString());
		}
	}

}