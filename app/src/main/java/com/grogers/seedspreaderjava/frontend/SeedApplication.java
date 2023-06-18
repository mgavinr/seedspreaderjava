package com.grogers.seedspreaderjava.frontend;

// created before any other class, there is normally no need to subclass.  In most
// cases static singletons can provide the same functionality in a more modular way.
// Your singleton then has a getInstance() method, but whatever you like go for
// This author added that last bit, of course.

import android.content.Context;

/* SeedApplication Comment based Markdown notes

JDK is 11 gradle settings

JDK 10 provides a frames document, I see, the one I (sorry some script) picked i.e. 11
does not, as it is old style, and frowned upon.
I thank the gods for that, and so do the many people posting about that online.
Similarly, when I was looking for some dev work todo in my company, i thought to
remove all documentation for new employees as they hadn't hired any
that week, and it was just taking up space.  My employees often thank me sincerely
for my efforts.

https://docs.oracle.com/javase/10/docs/api/index.html?overview-summary.html
has some frames, but didn't you use to be able to resize frames?
Let me check IBrowse, one second, i'll go to the attic.

I'm back, indeed you can; maybe it uses up too much code space and resources in new apps like firefox,
so it was removed?  I see also that minimize and maximise buttons are also gone .. due to space again, but,
this time, real-estate space on the UI?  (Nowadays instead you drag the window to the top to maximise)
Internet archives say
"Each page can have its own scroll bar, and be resized independently of the main browser window."
so it seems that feature was removed.  I thank thee again for that.
Similarly, when I was looking .., i thought I'd disconnect the heating supply and
cancel the contract with Board Gais, as it hasn't been cold or needed at all in at least a month
at work, at least.  It's just a wasting monthly expenditure.  Again, I am often thanked ..

 */

/* Hello loser can you please let us know what the API is that you are using and remove this
garbage so we can see it too
Also while you are at it the api ref java api and java api ref thank you
as variables too not comments.
 */

/**
 * Docu
 */
public class SeedApplication extends android.app.Application {
    private static SeedApplication instance;
    public SeedApplication() {
        instance = this;
    }
    public static Context getContext() {
        return instance.getApplicationContext();
    }


}
