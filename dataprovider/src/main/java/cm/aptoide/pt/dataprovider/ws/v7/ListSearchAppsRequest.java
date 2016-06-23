/*
 * Copyright (c) 2016.
 * Modified by Neurophobic Animal on 07/06/2016.
 */

package cm.aptoide.pt.dataprovider.ws.v7;

import java.util.LinkedList;
import java.util.List;

import cm.aptoide.accountmanager.AptoideAccountManager;
import cm.aptoide.pt.database.Database;
import cm.aptoide.pt.database.realm.Store;
import cm.aptoide.pt.dataprovider.ws.Api;
import cm.aptoide.pt.model.v7.ListSearchApps;
import cm.aptoide.pt.networkclient.WebService;
import cm.aptoide.pt.networkclient.okhttp.OkHttpClientFactory;
import cm.aptoide.pt.preferences.secure.SecurePreferences;
import cm.aptoide.pt.utils.AptoideUtils;
import io.realm.Realm;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import rx.Observable;

/**
 * Created by neuro on 26-04-2016.
 */
public class ListSearchAppsRequest extends V7<ListSearchApps, ListSearchAppsRequest.Body> {

	private ListSearchAppsRequest(OkHttpClient httpClient, Converter.Factory converterFactory, String aptoideId, String accessToken, int versionCode, String cdn) {
		super(new Body(aptoideId, accessToken, versionCode, cdn), httpClient, converterFactory, BASE_HOST);
	}

	public static ListSearchAppsRequest of(String query, boolean subscribedStores) {
		ListSearchAppsRequest listSearchAppsRequest = new ListSearchAppsRequest(OkHttpClientFactory.getSingletoneClient(), WebService.getDefaultConverter(), SecurePreferences
				.getAptoideClientUUID(), AptoideAccountManager.getAccessToken(), AptoideUtils.Core.getVerCode(), "pool");

		listSearchAppsRequest.body.setQuery(query);
		if (subscribedStores) {
			@Cleanup Realm realm = Database.get();
			LinkedList<Long> ids = new LinkedList<>();
			for (Store store : Database.StoreQ.getAll(realm)) {
				ids.add(store.getStoreId());
			}
			listSearchAppsRequest.body.setStoreIds(ids);
		}

		return listSearchAppsRequest;
	}

	@Override
	protected Observable<ListSearchApps> loadDataFromNetwork(Interfaces interfaces, boolean bypassCache) {
		return interfaces.listSearchApps(body, bypassCache);
	}

	@Data
	@Accessors(chain = true)
	@EqualsAndHashCode(callSuper = true)
	public static class Body extends BaseBody implements OffsetInterface<Body> {

		private String lang = Api.LANG;
		private Integer limit;
		private boolean mature;
		private int offset;
		private String q = Api.Q;
		private String query;
		private List<Long> storeIds;
		private Boolean trusted;

		public Body(String aptoideId, String accessToken, int aptoideVercode, String cdn) {
			super(aptoideId, accessToken, aptoideVercode, cdn);
		}
	}
}
