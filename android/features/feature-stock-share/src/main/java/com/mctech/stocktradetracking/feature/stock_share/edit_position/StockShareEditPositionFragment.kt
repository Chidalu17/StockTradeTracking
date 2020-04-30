package com.mctech.stocktradetracking.feature.stock_share.edit_position

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mctech.architecture.mvvm.x.core.ComponentState
import com.mctech.architecture.mvvm.x.core.ViewCommand
import com.mctech.architecture.mvvm.x.core.ktx.bindCommand
import com.mctech.architecture.mvvm.x.core.ktx.bindState
import com.mctech.library.keyboard.visibilitymonitor.extentions.closeKeyboard
import com.mctech.library.view.ktx.getValue
import com.mctech.stocktradetracking.domain.stock_share.entity.StockShare
import com.mctech.stocktradetracking.feature.stock_share.R
import com.mctech.stocktradetracking.feature.stock_share.StockShareCommand
import com.mctech.stocktradetracking.feature.stock_share.StockShareInteraction
import com.mctech.stocktradetracking.feature.stock_share.databinding.FragmentStockShareEditPriceBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StockShareEditPositionFragment : Fragment() {

	private val viewModel : StockShareEditPositionViewModel by viewModel()
	private var binding   : FragmentStockShareEditPriceBinding? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		setHasOptionsMenu(true)

		return FragmentStockShareEditPriceBinding.inflate(inflater, container, false).let {
			binding = it
			binding?.lifecycleOwner = this
			it.root
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		bindCommand(viewModel){ handleCommands(it) }
		bindState(viewModel.currentStockShare){ handleStockShareState(it) }
		bindListeners()
	}

	private fun handleStockShareState(state: ComponentState<StockShare>) {
		when(state){
			is ComponentState.Initializing -> {
				viewModel.interact(StockShareInteraction.List.OpenStockShareDetails(
					StockShareEditPositionFragmentArgs.fromBundle(requireArguments()).stockShare
				))
			}
			is ComponentState.Success -> {
				binding?.stockShare = state.result
				binding?.executePendingBindings()
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.stock_share_delete_menu, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when(item.itemId){
			R.id.menu_delete -> {
				viewModel.interact(StockShareInteraction.DeleteStockShare)
			}
		}

		return true
	}

	private fun handleCommands(command: ViewCommand) {
		when(command){
			is StockShareCommand.Back.FromEdit -> {
				findNavController().popBackStack()
			}
		}
	}

	private fun bindListeners() {
		binding?.let { binding ->
			binding.btUpdateStockPrice.setOnClickListener {
				viewModel.interact(
					StockShareInteraction.UpdateStockPrice(
						binding.etShareCode.getValue(),
						binding.etShareAmount.getValue().toLong(),
						binding.etSharePurchasePrice.getValue().toDouble(),
						binding.etSharePrice.getValue().toDouble()
					)
				)

				activity?.currentFocus?.run {
					if(this is EditText){
						context?.closeKeyboard(this)
					}
				}
			}
		}
	}
}