package com.assistant.loggerapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("======사용법 =======\n" +
                "1. OPEN을 누르면 floating 팝업이 뜨며 이 팝업은 어느 화면에서는 이동 가능합니다.\n" +
                "2. Logcat start를 누르면 지정된 경로에 폴더가 생성되면서 로그를 적재 합니다.\n" +
                "3. 10MB 단위로 새로운 파일을 만듭니다. (이전파일도 유지됨)\n" +
                "4. Logcat stop을 하면 현재까지의 로그로 파일을 만듭니다.\n" +
                "5. Log clear 를 하면 지금까지 적재된 로그 파일 전부 삭제합니다. (주의) 필요한 로그 취득 후 사용하세요 \n" +
                "6. minimum 을 선택 하면 팝업이 작아집니다. 팝업은 작아지더라도 로그기록은 계속됩니다.\n" +
                "7. close 를 하면 log 취득을 중단하고 팝업을 닫습니다. \n" +
                "8. 이제 사용할 준비가 되었으면 OPEN 을 누르고, 시스템의 홈버튼을 누르세요\n");
    }

    public LiveData<String> getText() {
        return mText;
    }
}