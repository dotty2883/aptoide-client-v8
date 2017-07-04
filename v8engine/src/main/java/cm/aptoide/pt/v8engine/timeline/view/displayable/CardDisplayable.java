package cm.aptoide.pt.v8engine.timeline.view.displayable;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.WindowManager;
import cm.aptoide.pt.dataprovider.model.v7.timeline.TimelineCard;
import cm.aptoide.pt.utils.AptoideUtils;
import cm.aptoide.pt.v8engine.R;
import cm.aptoide.pt.v8engine.timeline.TimelineAnalytics;
import cm.aptoide.pt.v8engine.timeline.TimelineSocialActionData;
import cm.aptoide.pt.v8engine.timeline.view.ShareCardCallback;
import cm.aptoide.pt.v8engine.view.recycler.displayable.Displayable;

/**
 * Created by jdandrade on 29/11/2016.
 */

public abstract class CardDisplayable extends Displayable {

  public static final String SHARE = "Share";
  public static final String LIKE = "Like";
  private TimelineCard timelineCard;
  private TimelineAnalytics timelineAnalytics;
  private TimelineSocialActionData timelineSocialActionData;
  private WindowManager windowManager;

  CardDisplayable() {
  }

  CardDisplayable(TimelineCard timelineCard, TimelineAnalytics timelineAnalytics,
      WindowManager windowManager) {
    this.timelineCard = timelineCard;
    this.timelineAnalytics = timelineAnalytics;
    this.windowManager = windowManager;
  }

  public TimelineCard getTimelineCard() {
    return timelineCard;
  }

  @Override protected Configs getConfig() {
    return new Configs(1, true);
  }

  public int getMarginWidth(Context context, int orientation) {
    if (!context.getResources()
        .getBoolean(R.bool.is_this_a_tablet_device)) {
      return 0;
    }

    int width = AptoideUtils.ScreenU.getCachedDisplayWidth(orientation, windowManager);

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
      return (int) (width * 0.2);
    } else {
      return (int) (width * 0.1);
    }
  }

  /**
   * Shares the card and generates a corresponding Social card.
   *
   * @param cardId
   * @param shareCardCallback Listens to the result of the share operation. Pass null if you want
   * to
   * @param resources
   */
  public abstract void share(String cardId, boolean privacyResult,
      ShareCardCallback shareCardCallback, Resources resources);

  public abstract void share(String cardId, ShareCardCallback shareCardCallback,
      Resources resources);

  public abstract void like(Context context, String cardType, int rating, Resources resources);

  public abstract void like(Context context, String cardId, String cardType, int rating,
      Resources resources);

  public void sendSocialActionEvent(String actionValue) {
    timelineAnalytics.sendSocialCardPreviewActionEvent(actionValue);
  }

  public TimelineAnalytics getTimelineAnalytics() {
    return timelineAnalytics;
  }

  public TimelineSocialActionData getTimelineSocialActionObject(String cardType, String action,
      String socialAction, String packageName, String publisher, String title) {
    return new TimelineSocialActionData(cardType, action, socialAction, packageName, publisher,
        title);
  }
}
