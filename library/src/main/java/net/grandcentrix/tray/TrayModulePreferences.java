/*
 * Copyright (C) 2015 grandcentrix GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.grandcentrix.tray;

import net.grandcentrix.tray.accessor.TrayPreference;
import net.grandcentrix.tray.provider.TrayItem;
import net.grandcentrix.tray.storage.TrayStorage;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Collection;

/**
 * Created by pascalwelsch on 11/20/14.
 * <p>
 * A {@link net.grandcentrix.tray.accessor.Preference} where the module name depends on the
 * developer. Extending this class should be preferred compared to the usage of the {@link
 * net.grandcentrix.tray.TrayAppPreferences}.
 * <p>
 * This class gives the developer the opportunity to remove whole modules without knowing each
 * single preference key.
 * <p>
 * Communicates with the {@link net.grandcentrix.tray.storage.TrayStorage} to store the preferences
 * into a {@link android.content.ContentProvider}
 */
public abstract class TrayModulePreferences extends TrayPreference {

    public TrayModulePreferences(@NonNull final Context context, @NonNull final String module,
            final int version) {
        super(new TrayStorage(context, module), version);
    }

    /**
     * imports all data from an old storage. Use this if you have changed the module name
     * <p>
     * Call this in {@link #onCreate(int)}. The created and updated fields of the old {@link
     * TrayItem}s will be lost. The old data gets deleted completely.
     *
     * @param oldName the name of the old preference
     */
    public void oldName(final String oldName) {
        final TrayStorage oldStorage = new TrayStorage(getContext(), oldName);
        final Collection<TrayItem> items = oldStorage.getAll();
        if (items.size() > 0) {
            for (final TrayItem trayItem : items) {
                getStorage().put(trayItem.key(), trayItem.migratedKey(), trayItem.value());
            }
            oldStorage.wipe();
        }
    }

    protected Context getContext() {
        return ((TrayStorage) getStorage()).getContext();
    }
}
