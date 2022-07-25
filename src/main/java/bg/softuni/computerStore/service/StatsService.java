package bg.softuni.computerStore.service;

import bg.softuni.computerStore.model.view.stats.StatsViewModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class StatsService {
    private int anonymousRequests, authRequests;
    private StatsViewModel statsViewModel;

    public void onRequest(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication != null && (authentication.getPrincipal() instanceof UserDetails)) {
            authRequests++;
        } else {
            anonymousRequests++;
        }
    }

    public StatsViewModel getStats(){
        return new StatsViewModel(authRequests, anonymousRequests);
    }
}
