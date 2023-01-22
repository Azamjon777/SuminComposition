package com.example.sumincomposition.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sumincomposition.R
import com.example.sumincomposition.databinding.FragmentGameBinding
import com.example.sumincomposition.domain.entity_models.GameResult
import com.example.sumincomposition.domain.entity_models.Level

class GameFragment : Fragment() {
    private val args by navArgs<GameFragmentArgs>()

    /*  инициализация таким образом помогает тем образом, что инициализация будет при первом обращении к
        переменному, а не тогда когда приложение создастся. А мы будем обращаться к переменному в
        onViewCreated(). Ато был бы вызван requireActivity(), которого еще нет и приложение бы упало.
        Это как бы гарантия что приложение не упадет*/
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(
            this,
            AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setClickListenersToOptions()
        viewModel.startGame(args.level)
    }

    private fun setClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                //здесь передаем число преобразованное из строки
                //А chooseAnswer() проверяет правильно ли мы ответили
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            formattedTime.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }

            question.observe(viewLifecycleOwner) {
                binding.tvSum.text = it.sum.toString()
                binding.tvLeftNumber.text = it.visibleNumber.toString()
                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text =
                        it.options[i].toString()//в options приходят только 2 элемента
                }
            }

            viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
                binding.progressBar.setProgress(it, true)
            }

            viewModel.enoughCount.observe(viewLifecycleOwner) {
                //устанавливаем цвет у текста который считает наше кол-во правильных ответов
                binding.tvAnswersProgress.setTextColor(getColorByState(it))
            }

            viewModel.enoughPercent.observe(viewLifecycleOwner) {
                //устанавливаем цвет у прогрессбара
                val color = getColorByState(it)
                binding.progressBar.progressTintList = ColorStateList.valueOf(color)
            }

            viewModel.minPercentSecondaryProgress.observe(viewLifecycleOwner) {
                binding.progressBar.secondaryProgress = it
            }

            viewModel.gameResult.observe(viewLifecycleOwner) {
                launchGameFinishedFragment(it)
            }

            viewModel.progressAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.text = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult))
    }
}