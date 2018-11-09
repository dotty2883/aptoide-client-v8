package cm.aptoide.pt.commentdetail;

import cm.aptoide.pt.comment.data.Comment;
import cm.aptoide.pt.presenter.View;
import rx.Observable;

public interface CommentDetailView extends View {

  void showCommentModel(CommentDetailViewModel viewModel);

  void showLoading();

  void hideLoading();

  Observable<Comment> commentClicked();

  void hideKeyboard();
}

