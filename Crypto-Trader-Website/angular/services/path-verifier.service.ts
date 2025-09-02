import {Injectable} from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class PathVerifierService {
    constructor() {

    }

    public isAngularAsset(path: string) {
       return path.includes("assets/");
    }
    
    public isValidPath(path: string): boolean {
        try {
            new URL(path);
            return true;
        } catch (exception) {
            return false;
        }
    }

    
    public async angularAssetExists(path: string): Promise<boolean> {
        return await this.isValidLink(path);
    }
    
    public async isValidLink(link: string): Promise<boolean> {
        const response: Response = await fetch(link);
        return response.ok;
    }
}