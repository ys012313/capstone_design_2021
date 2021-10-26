package com.cm.taxi.di

import com.cm.taxi.view.complete.BoardginCompleteViewModel
import com.cm.taxi.view.home.HomeViewModel
import com.cm.taxi.view.home.today.TodayMateViewModel
import com.cm.taxi.view.login.LoginViewModel
import com.cm.taxi.view.mate.SearchMateViewModel
import com.cm.taxi.view.mate.group.CreateMateGroupViewModel
import com.cm.taxi.view.mate.join.JoinMateGroupViewModel
import com.cm.taxi.view.mate.scan.ScanMateViewModel
import com.cm.taxi.view.setting.SettingViewModel
import com.cm.taxi.view.setting.latest.LatestViewModel
import com.cm.taxi.view.setting.statistics.StatisticsViewModel
import com.cm.taxi.view.signup.SignupViewModel
import com.cm.taxi.view.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { SplashViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { SignupViewModel(get()) }
    viewModel { SettingViewModel(get()) }
    viewModel { SearchMateViewModel(get()) }
    viewModel { CreateMateGroupViewModel(get()) }
    viewModel { JoinMateGroupViewModel(get()) }
    viewModel { TodayMateViewModel(get()) }
    viewModel { BoardginCompleteViewModel(get()) }
    viewModel { StatisticsViewModel(get()) }
    viewModel { LatestViewModel(get()) }
    viewModel { ScanMateViewModel(get()) }
}