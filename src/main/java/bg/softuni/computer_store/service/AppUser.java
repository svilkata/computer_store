package bg.softuni.computer_store.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AppUser implements UserDetails {

  private final String password;
  private final String username;
  private final Collection<? extends GrantedAuthority> authorities;

  public AppUser(
      String userName,
      String password,
      Collection<? extends GrantedAuthority> authorities
  ) {
    this.password = password;
    this.username = userName;
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return "AppUser{" +
        "password='" + password + '\'' +
        ", userName='" + username + '\'' +
        ", authorities=" + authorities +
        '}';
  }
}
