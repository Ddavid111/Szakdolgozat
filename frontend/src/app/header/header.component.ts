import {Component} from '@angular/core';
import {AuthService} from "../_services/auth.service";
import {Router} from "@angular/router";
import {UserService} from "../_services/user.service";

@Component({
  selector: 'navigation-bar',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  constructor(private authService: AuthService,
              private router: Router,
              private userService: UserService) {
  }

  ngOnInit(): void {
    if (!this.userService.roleMatch(['ADMIN', 'Hallgató', 'Bíráló', 'Témavezető', 'Jegyző', 'Elnök'])) {
      this.router.navigate(['/login']);
    }
  }

  // returns the roleId of the logged in user
  // problem: hallgato has roleId 0 which means false here
  public isLoggedIn() {
    return this.authService.isLoggedIn()
  }

  public logout() {
    this.authService.clear();
    this.router.navigate(['/login']);
  }


  hasRole(role: string): boolean {
    return this.userService.roleMatch([role]);
  }

}
