//package com.example.swipablecardtest.informationhub;
//
//import android.app.Application;
//
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import java.util.List;
//
//public class ContentViewModel extends AndroidViewModel {
//    private static final int DEFAULT_LIMIT = 5;
//    private ContentRepository contentRepository;
//    private final LiveData<List<Content>> allContents;
//    private MutableLiveData<List<Content>> limitedContents;
//
//    public ContentViewModel(Application application) {
//        super(application);
//        this.contentRepository = new ContentRepository(application);
//        this.allContents = contentRepository.getAllContents();
//        limitedContents = new MutableLiveData<>();
//
//        allContents.observeForever(contents -> {
//            if (contents != null) {
//                limitedContents.setValue(contents.subList(0, Math.min(contents.size(), DEFAULT_LIMIT)));
//            }
//        });
//    }
//
//    LiveData<List<Content>> getAllContents() {
//        return allContents;
//    }
//
//    MutableLiveData<List<Content>> getLimitedContents() { return limitedContents;
//    }
//
//    void insert(Content content) {
//        contentRepository.insert(content);
//    }
//}
