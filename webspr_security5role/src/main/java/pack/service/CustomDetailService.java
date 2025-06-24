package pack.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pack.entity.MyUser;

import java.util.ArrayList;
import java.util.List;

// 유저 정보를 가져와 처리
@Service
public class CustomDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username : " + username);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String role = "";
        if(username.equals("guest")) {
            role = "ROLE_USER";
        } else if (username.equals("batman")) {
            role = "ROLE_STAFF";
        } else if (username.equals("superman")) {
            role = "ROLE_ADMIN";
        }

        MyUser myUser = MyUser.builder()
                .id(1).userName(username)
                .password(encoder.encode("1234"))
                        .role(role).build();

        // 여러 개의 권한을 담는 컬렉션
        List<GrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority(myUser.getRole()));

        UserDetails userDetails = new User(myUser.getUserName(), myUser.getPassword(), authList);
        return userDetails;
    }
}
