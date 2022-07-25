package bg.softuni.computerStore.model.view.stats;

public class StatsViewModel {
    private final int authRequests;
    private final int anonymousRequests;

    public StatsViewModel(int authRequests, int anonymousRequests) {
        this.authRequests = authRequests;
        this.anonymousRequests = anonymousRequests;
    }

    public int getTotalRequests() {
        return authRequests + anonymousRequests;
    }

    public int getAuthRequests() {
        return authRequests;
    }

    public int getAnonymousRequests() {
        return anonymousRequests;
    }
}


