package com.example.timeconversionapplication

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.example.timeconversionapplication.databinding.ApiProductShowBinding
import org.json.JSONObject

class ProductDialogFragment : DialogFragment() {

    interface OnProductSelectedListener {
        fun onProductSelected(name: String, price: String)
    }

    private var listener: OnProductSelectedListener? = null
    private var response: String? = null
    private var _binding: ApiProductShowBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            response = it.getString(ARG_RESPONSE)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = targetFragment as? OnProductSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ApiProductShowBinding.inflate(inflater, container, false)
        val view = binding.root

        response?.let {
            val items = JSONObject(it).getJSONArray("items")
            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)

                // Remove HTML tags and text inside square brackets
                val title = Html.fromHtml(item.getString("title")).toString().replace(Regex("\\[.*?\\]"), "")
                Log.d("API", "Processed title: $title")

                val radioButton = RadioButton(context).apply {
                    text = "$title :${item.getString("lprice")}원"
                    tag = Pair(title, item.getString("lprice"))
                }
                binding.radioGroupProducts.addView(radioButton)

                // Add a divider
                val divider = View(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                    )
                    setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                }
                binding.radioGroupProducts.addView(divider)
            }
        }

        binding.confirmButton.setOnClickListener {
            val selectedRadioButton = view.findViewById<RadioButton>(binding.radioGroupProducts.checkedRadioButtonId)
            if (selectedRadioButton != null) {
                val selectedData = selectedRadioButton.tag as Pair<String, String>
                listener?.onProductSelected(selectedData.first, selectedData.second)
                dismiss()
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_RESPONSE = "response"

        fun newInstance(response: String) = ProductDialogFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_RESPONSE, response)
            }
        }
    }
}
