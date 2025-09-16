import {
    AfterViewInit, ChangeDetectorRef,
    Component,
    Input,
    OnInit,
    ViewChild
} from "@angular/core";
import {defaultAvatar, ImageAsset} from "../../../../assets/imageAssets";
import {SsImgComponent} from "@theoliverlear/angular-suite";

@Component({
    selector: 'profile-picture',
    standalone: false,
    templateUrl: './profile-picture.component.html',
    styleUrls: ['./profile-picture.component.scss']
})
export class ProfilePictureComponent implements AfterViewInit {
    @Input() userId: number = 0;
    @Input() isHeadlineProfilePicture: boolean = false;
    @Input() imageAsset: ImageAsset = defaultAvatar;
    @ViewChild('profilePictureImage') profilePictureImage: SsImgComponent;
    constructor(private changeDetection: ChangeDetectorRef) {
        
    }
    ngAfterViewInit() {
        
    }

    protected readonly defaultAvatar = defaultAvatar;
}