export interface Persona {
    id: string;
    name: string;
    tagline: string;
    description: string;
    // TODO: Replace with concrete type. Probably ImageAsset.
    icon: any;
    accentColor: string;
    suggestedTier: string;
    traits: string[];
}
