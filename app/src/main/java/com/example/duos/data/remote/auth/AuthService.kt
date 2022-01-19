package com.example.duos.data.remote.auth


object AuthService {
    //    fun signUp(signUpView: SignUpView ,user: User) {
//        val authService = retrofit.create(AuthRetrofitInterface::class.java)
//
//        signUpView.onSignUpLoading()
//
//        authService.signUp(user).enqueue(object : Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//
//                val resp = response.body()!!
//
//                when(resp.code){
//                    1000 -> signUpView.onSignUpSuccess()
//                    else -> signUpView.onSignUpFailure(resp.code, resp.message)
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Log.d("$TAG/API-ERROR", t.message.toString())
//
//                signUpView.onSignUpFailure(400, "네트워크 오류가 발생했습니다.")
//            }
//        })
//    }
//
//    fun login(loginView: LoginView, user: User) {
//        val authService = retrofit.create(AuthRetrofitInterface::class.java)
//
//        loginView.onLoginLoading()
//
//        authService.login(user).enqueue(object : Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                val resp = response.body()!!
//
//                when(resp.code){
//                    1000 -> loginView.onLoginSuccess(resp.result!!)
//                    else -> loginView.onLoginFailure(resp.code, resp.message)
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Log.d("$TAG/API-ERROR", t.message.toString())
//
//                loginView.onLoginFailure(400, "네트워크 오류가 발생했습니다.")
//            }
//        })
//    }
//
//    fun autoLogin(splashView: SplashView) {
//        val authService = retrofit.create(AuthRetrofitInterface::class.java)
//
//        splashView.onAutoLoginLoading()
//
//        authService.autoLogin().enqueue(object : Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                val resp = response.body()!!
//
//                when(resp.code){
//                    1000 -> splashView.onAutoLoginSuccess()
//                    else -> splashView.onAutoLoginFailure(resp.code, resp.message)
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Log.d("$TAG/API-ERROR", t.message.toString())
//
//                splashView.onAutoLoginFailure(400, "네트워크 오류가 발생했습니다.")
//            }
//        })
//    }
//
//    fun getLargeLocal(localProcessing : LargeLocalSearchView){
//        var BASE_URL = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/"
//        val  retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val authService = retrofit.create(AuthRetrofitInterface::class.java)
//
//        authService.getLargeReg(regcode_pattern = "*00000000").enqueue(object : Callback<AuthResponse>{
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                Log.d("resp-success","성공")
//                Log.d("resp-string", response.body().toString())
//
//                // room 에 local 저장하기
//                response.body()?.let { localProcessing.LargeLocalToRoomDB(it.reg) }
//
//                // 시,도를 화면에 보여주기
//                localProcessing.LargeLocalSearchOnView()
//
//
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Log.d("resp-failure","실패")
//            }
//        })
//    }

}