package com.example.sumincomposition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sumincomposition.databinding.FragmentGameBinding
import com.example.sumincomposition.domain.entity_models.GameResult

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

        /*эти две строчки для того чтобы xml вовремя отписывался от всякиx livedata во viewModel,
        во время переворота экрана*/
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        observeViewModel()
        viewModel.startGame(args.level)
    }

    private fun observeViewModel() {
        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }
}